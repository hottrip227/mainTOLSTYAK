# Визуальные ассеты 0.3

`front.png`, `back.png`, `quas.png` — **отдельные рендеры реальной геометрии и текстур прототипа, не скриншоты Minecraft**. Снимки самого клиента, если приложены, называются `minecraft-*.png`.

- `src/main/resources/assets/ashes_of_archmage/textures/entity/archmage.png` — редактируемый 64×64 скин.
- `archmage_outfit.png` в той же папке — 256×256 атлас объёмных деталей.
- `outfit-geometry.json` — описание 49 кубоидов костюма.
- `src/main/java/dev/archmage/client/ArchmageOutfitGeometry.java` — Java-геометрия, используемая непосредственно рендером.
- `assets/.../models/item/quas_orb.json` и `textures/item/quas_orb.png` — 256 элементов сферы и 32×32 ледяная текстура.
- `tools/make_visual_assets.py` — воспроизводимый генератор перечисленных исходных ассетов. Запуск из любого каталога: `python tools/make_visual_assets.py` (Python + Pillow). После правок параметров пересобери JAR.

Генератор перезаписывает эти ассеты, поэтому ручные правки сначала сохрани отдельно. Одна серебристая причёска; никакого выбора вариантов пока нет. Костюм не заменяет скин аккаунта. Все упакованные текстуры созданы для прототипа, картинки/модели Valve не использованы.
