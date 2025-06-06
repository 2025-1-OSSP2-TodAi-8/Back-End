# Generated by Django 5.2 on 2025-05-23 01:00

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Diary',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateField(null=True)),
                ('audio', models.FileField(null=True, upload_to='diary/audio/')),
                ('summary', models.TextField(null=True)),
                ('marking', models.BooleanField(default=False)),
            ],
        ),
    ]
