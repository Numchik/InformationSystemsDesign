"""Лабораторная работа №5: оценка надежности ПО по ГОСТ 28195-89.

Вариант 2. Подкласс ПО 503, фаза обслуживания (сопровождения).
"""

from __future__ import annotations

import random
from dataclasses import dataclass
from statistics import fmean


SEED = 20261002
FAILURES = 6
EXPERIMENTS = 1200
RESTORE_INTERVAL = (0.8, 1.4)
RESTORE_LIMIT = 0.95
TRANSFORM_INTERVAL = (8.0, 12.0)
TRANSFORM_LIMIT = 14.0
BASE_CRITERION = 0.94


@dataclass(frozen=True)
class QualityResult:
    restore_times: list[float]
    transform_times: list[float]
    h0401: float
    h0501: float
    h0502: float
    metric_4: float
    metric_5: float
    criterion: float
    relative_criterion: float
    reliability_factor: float


def limited_time_score(actual: float, allowed: float) -> float:
    """Оценка временного показателя по таблице 5 ГОСТ 28195-89."""
    return 1.0 if actual <= allowed else allowed / actual


def calculate(seed: int = SEED) -> QualityResult:
    """Сформировать выборки варианта 2 и вычислить фактор надежности."""
    rng = random.Random(seed)
    restore_times = [rng.uniform(*RESTORE_INTERVAL) for _ in range(100)]
    transform_times = [rng.uniform(*TRANSFORM_INTERVAL) for _ in range(200)]

    h0401 = 1.0 - FAILURES / EXPERIMENTS
    h0501 = limited_time_score(fmean(restore_times), RESTORE_LIMIT)
    h0502 = fmean(limited_time_score(t, TRANSFORM_LIMIT) for t in transform_times)

    # Весовые коэффициенты внутри каждой группы равны.
    metric_4 = h0401
    metric_5 = fmean((h0501, h0502))
    criterion = fmean((metric_4, metric_5))
    relative_criterion = criterion / BASE_CRITERION
    reliability_factor = relative_criterion

    return QualityResult(
        restore_times=restore_times,
        transform_times=transform_times,
        h0401=h0401,
        h0501=h0501,
        h0502=h0502,
        metric_4=metric_4,
        metric_5=metric_5,
        criterion=criterion,
        relative_criterion=relative_criterion,
        reliability_factor=reliability_factor,
    )


def main() -> None:
    result = calculate()
    print("Лабораторная работа №5, вариант 2")
    print(f"Среднее время восстановления: {fmean(result.restore_times):.6f} с")
    print(f"Среднее время преобразования: {fmean(result.transform_times):.6f} с")
    print(f"H0401 = {result.h0401:.6f}")
    print(f"H0501 = {result.h0501:.6f}")
    print(f"H0502 = {result.h0502:.6f}")
    print(f"Метрика 4 = {result.metric_4:.6f}")
    print(f"Метрика 5 = {result.metric_5:.6f}")
    print(f"Абсолютный показатель критерия = {result.criterion:.6f}")
    print(f"Относительный показатель критерия = {result.relative_criterion:.6f}")
    print(f"Фактор надежности = {result.reliability_factor:.6f}")


if __name__ == "__main__":
    main()
