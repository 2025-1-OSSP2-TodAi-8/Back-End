# Generated by Django 5.2 on 2025-05-07 07:15

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('diary', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='diary',
            name='summary',
            field=models.TextField(default=False),
            preserve_default=False,
        ),
    ]
