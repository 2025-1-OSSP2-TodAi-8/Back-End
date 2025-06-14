from rest_framework.decorators import api_view, permission_classes, parser_classes
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework import status

from django.http import FileResponse
from django.core.files import File
from django.core.files.temp import NamedTemporaryFile
from moviepy.editor import AudioFileClip

import uuid
import tempfile
import os
import mimetypes
import datetime


from .models import Diary

from .utils import full_multimodal_analysis


@api_view(["POST"])
@permission_classes([IsAuthenticated])
@parser_classes([MultiPartParser, FormParser])
def record(request):
    user = request.user
    date_str = request.data.get("date")
    audio = request.FILES.get("audio")

    if not date_str or not audio:
        return Response(
            {"success": 0, "emotion": [], "text": "데이터 누락"},
            status=status.HTTP_400_BAD_REQUEST,
        )

    try:
        date = datetime.datetime.strptime(date_str, "%Y-%m-%d").date()
    except ValueError:
        return Response(
            {"success": 0, "emotion": [], "text": "날짜형식 불일치"},
            status=status.HTTP_400_BAD_REQUEST,
        )

    diary = Diary.objects.filter(user=user, date=date).first()

    if diary:
        if diary.audio:
            diary.audio.delete(save=False)
        diary.date = date
    else:
        diary = Diary.objects.create(user=user, date=date, emotion=[0] * 6)

    ext = os.path.splitext(audio.name)[1].lower()
    if ext == ".mp4":
        with tempfile.NamedTemporaryFile(suffix=".mp4", delete=False) as tmp_input:
            for chunk in audio.chunks():
                tmp_input.write(chunk)
            tmp_input_path = tmp_input.name

        try:
            audioclip = AudioFileClip(tmp_input_path)
            tmp_wav = NamedTemporaryFile(suffix=".wav", delete=False)
            audioclip.write_audiofile(
                tmp_wav.name, fps=16000, verbose=False, logger=None
            )
            audioclip.close()

            wav_file_name = f"{uuid.uuid4()}.wav"
            with open(tmp_wav.name, "rb") as f:
                diary.audio.save(wav_file_name, File(f), save=True)

            audio_path = diary.audio.path
        except Exception as e:
            return Response(
                {"success": 0, "text": f"mp4 처리 오류: {str(e)}"}, status=500
            )
        finally:
            os.remove(tmp_input_path)
            os.remove(tmp_wav.name)

    else:
        diary.audio = audio
        diary.save(update_fields=["audio"])
        audio_path = diary.audio.path

    try:
        summary, emotion = full_multimodal_analysis(audio_path)
    except:
        return Response(
            {"success": 0, "text": "파이프라인 에러"},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR,
        )
    diary.summary = summary
    diary.emotion = emotion
    diary.save()

    return Response(
        {
            "success": 1,
            "emotion": emotion,
            "summary": diary.summary or "",
        },
        status=status.HTTP_200_OK,
    )


@api_view(["POST"])
@permission_classes([IsAuthenticated])
def get_record(request):
    user = request.user
    year = request.data.get("year")
    month = request.data.get("month")
    day = request.data.get("day")

    # 1) 필수 파라미터 검사
    if year is None or month is None or day is None:
        return Response(
            {"success": 0, "message": "year, month, day 모두 필수입니다."}, status=400
        )
    # 2) 정수 변환
    try:
        y, m, d = int(year), int(month), int(day)
        target_date = datetime.date(y, m, d)
    except (ValueError, TypeError):
        return Response(
            {"success": 0, "message": "year, month, day는 정수여야 합니다."}, status=400
        )
    # 3) 해당 날짜의 Diary 조회
    diary = Diary.objects.filter(user=user, date=target_date).first()
    if not diary or not diary.audio:
        return Response(
            {"success": 0, "message": f"{target_date}의 음성 파일이 없습니다."},
            status=404,
        )
    # 4) 파일 열기
    file_path = diary.audio.path
    file_handle = open(file_path, "rb")

    # 5) Content-Type 추출
    content_type, _ = mimetypes.guess_type(file_path)
    if content_type is None:
        content_type = "application/octet-stream"

    # 6) FileResponse 반환
    response = FileResponse(file_handle, content_type=content_type)
    response["Content-Disposition"] = (
        f'attachment; filename="{os.path.basename(file_path)}"'
    )
    return response


@api_view(["POST"])
@permission_classes([IsAuthenticated])
def marking(request):

    user = request.user
    date_str = request.data.get("date")
    if not date_str:
        return Response(
            {"success": 0, "message": "date 누락"},
            status=status.HTTP_400_BAD_REQUEST,
        )

    # 날짜 파싱
    try:
        date = datetime.datetime.strptime(date_str, "%Y-%m-%d").date()
    except (ValueError, TypeError):
        return Response(
            {"success": 0, "message": "잘못된 date 형식 (YYYY-MM-DD)"},
            status=status.HTTP_400_BAD_REQUEST,
        )

    diary = Diary.objects.filter(user=user, date=date).first()

    if not diary:
        return Response(
            {"success": 0, "message": f"{date_str}의 일기를 찾을 수 없습니다."},
            status=status.HTTP_404_NOT_FOUND,
        )

    diary.marking = not diary.marking
    diary.save(update_fields=["marking"])

    return Response(
        {"success": 1, "marking": diary.marking},
        status=status.HTTP_200_OK,
    )


EMOTION_LABELS = ["행복", "슬픔", "화남", "놀람", "공포", "혐오"]


@api_view(["POST"])
@permission_classes([IsAuthenticated])
def get_marked_year(request):
    user = request.user
    year = request.data.get("year")

    if year is None:
        return Response({"error": "year 필수가 누락되었습니다."}, status=400)
    try:
        year = int(year)
    except (ValueError, TypeError):
        return Response({"error": "year는 정수여야 합니다."}, status=400)

    diaries = Diary.objects.filter(
        user=user,
        marking=True,
        date__year=year,
    ).order_by("date")

    emotions = []
    for d in diaries:
        probs = d.emotion or []
        idx = max(range(len(probs)), key=lambda i: probs[i])
        label = EMOTION_LABELS[idx]

        emotions.append({"date": d.date.isoformat(), "emotion": label})

    return Response({"emotions": emotions}, status=200)


@api_view(["POST"])
@permission_classes([IsAuthenticated])
def get_marked_month(request):
    user = request.user
    year = request.data.get("year")
    month = request.data.get("month")

    if year is None or month is None:
        return Response({"error": "year or month 필수가 누락되었습니다."}, status=400)
    try:
        year = int(year)
        month = int(month)
    except (ValueError, TypeError):
        return Response({"error": "year, month는 정수여야 합니다."}, status=400)

    diaries = Diary.objects.filter(
        user=user,
        marking=True,
        date__year=year,
        date__month=month,
    ).order_by("date")

    emotions = []
    for d in diaries:
        probs = d.emotion or []
        if len(probs) == len(EMOTION_LABELS):
            idx = max(range(len(probs)), key=lambda i: probs[i])
            label = EMOTION_LABELS[idx]
        else:
            label = ""
        emotions.append({"date": d.date.isoformat(), "emotion": label})

    return Response({"emotions": emotions}, status=200)
