# Generated by Django 5.2 on 2025-05-01 13:01

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='test',
            fields=[
                ('test', models.CharField(max_length=50, primary_key=True, serialize=False)),
            ],
        ),
    ]
