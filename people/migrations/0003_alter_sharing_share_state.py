# Generated by Django 5.2 on 2025-05-31 10:31

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('people', '0002_alter_sharing_share_state'),
    ]

    operations = [
        migrations.AlterField(
            model_name='sharing',
            name='share_state',
            field=models.CharField(choices=[('unmatched', '연동 대기'), ('matched', '연동 완료'), ('rejected', '연동 거절')], default='unmatched', max_length=10),
        ),
    ]
