# Generated by Django 5.2 on 2025-05-15 00:23

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0006_rename_user_id_people_string_id'),
    ]

    operations = [
        migrations.AddField(
            model_name='sharing',
            name='relation',
            field=models.CharField(choices=[('mother', '엄마'), ('father', '아빠'), ('daughter', '딸'), ('son', '아들'), ('doctor', '의료진'), ('default', '기타')], default='default', max_length=20),
        ),
    ]
