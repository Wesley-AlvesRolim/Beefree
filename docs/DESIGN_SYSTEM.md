# Design System

## 1. Colors

### Light Theme (`LightColorScheme`)

| Role                    | Token                     | Hex       |
| :---------------------- | :------------------------ | :-------- |
| primary                 | `Primary`                 | `#705D00` |
| onPrimary               | `OnPrimary`               | `#FFFFFF` |
| primaryContainer        | `PrimaryContainer`        | `#FFD700` |
| onPrimaryContainer      | `OnPrimaryContainer`      | `#705E00` |
| secondary               | `Secondary`               | `#4C616C` |
| secondaryContainer      | `SecondaryContainer`      | `#CFE6F2` |
| onSecondaryContainer    | `OnSecondaryContainer`    | `#526772` |
| tertiary                | `Tertiary`                | `#75584D` |
| tertiaryContainer       | `TertiaryContainer`       | `#F9D2C4` |
| onTertiaryContainer     | `OnTertiaryContainer`     | `#75594E` |
| surface                 | `SurfaceLight`            | `#FAFAF5` |
| onSurface               | `OnSurface`               | `#1A1C19` |
| onSurfaceVariant        | `OnSurfaceVariant`        | `#4D4732` |
| surfaceVariant          | `SurfaceVariant`          | `#E2E3DE` |
| surfaceContainerLowest  | `SurfaceContainerLowest`  | `#FFFFFF` |
| surfaceContainerLow     | `SurfaceContainerLow`     | `#F4F4EF` |
| surfaceContainer        | `SurfaceContainer`        | `#EEEEE9` |
| surfaceContainerHigh    | `SurfaceContainerHigh`    | `#E8E8E4` |
| surfaceContainerHighest | `SurfaceContainerHighest` | `#E2E3DE` |
| outline                 | `Outline`                 | `#7E775F` |
| outlineVariant          | `OutlineVariant`          | `#D0C6AB` |
| error                   | `Error`                   | `#BA1A1A` |
| errorContainer          | `ErrorContainer`          | `#FFDAD6` |
| onErrorContainer        | `OnErrorContainer`        | `#93000A` |
| success _(custom)_      | `Success`                 | `#2D6A4F` |

### Dark Theme (`DarkColorScheme`)

| Role                    | Token                         | Hex       |
| :---------------------- | :---------------------------- | :-------- |
| primary                 | `DarkPrimary`                 | `#E9C400` |
| onPrimary               | `DarkOnPrimary`               | `#3A3000` |
| primaryContainer        | `DarkPrimaryContainer`        | `#FFE16D` |
| onPrimaryContainer      | `DarkOnPrimaryContainer`      | `#221B00` |
| secondary               | `DarkSecondary`               | `#B4CAD6` |
| secondaryContainer      | `DarkSecondaryContainer`      | `#354A53` |
| onSecondaryContainer    | `DarkOnSecondaryContainer`    | `#CFE6F2` |
| tertiary                | `DarkTertiary`                | `#E4BEB2` |
| tertiaryContainer       | `DarkTertiaryContainer`       | `#5B4137` |
| onTertiaryContainer     | `DarkOnTertiaryContainer`     | `#FFDBCE` |
| surface                 | `DarkSurface`                 | `#121212` |
| onSurface               | `DarkOnSurface`               | `#E2E3DE` |
| onSurfaceVariant        | `DarkOnSurfaceVariant`        | `#D0C6AB` |
| surfaceVariant          | `DarkSurfaceVariant`          | `#4D4732` |
| surfaceContainerLowest  | `DarkSurfaceContainerLowest`  | `#0C0E0B` |
| surfaceContainerLow     | `DarkSurfaceContainerLow`     | `#1A1C19` |
| surfaceContainer        | `DarkSurfaceContainer`        | `#1E201D` |
| surfaceContainerHigh    | `DarkSurfaceContainerHigh`    | `#282B27` |
| surfaceContainerHighest | `DarkSurfaceContainerHighest` | `#333532` |
| outline                 | `DarkOutline`                 | `#989178` |
| outlineVariant          | `DarkOutlineVariant`          | `#4D4732` |
| error                   | `DarkError`                   | `#FFB4AB` |
| errorContainer          | `DarkErrorContainer`          | `#93000A` |
| onErrorContainer        | `DarkOnErrorContainer`        | `#FFDAD6` |

### Color Rules

- `primary` (#705D00 light / #E9C400 dark) → action buttons, progress, active states.
- `primaryContainer` (yellow) → highlights, chips, badges. **Never use as button background.**
- No hardcoded colors in Compose. Always reference `MaterialTheme.colorScheme.*`.
- No 1px solid borders for sectioning — use tonal background shifts instead.

---

## 2. Typography

Fonts: **Manrope** (headlines) · **Inter 18pt** (body + labels)

| Composable          | Style token      | Font    | Weight        | Size | Line height | Tracking | Use for                |
| :------------------ | :--------------- | :------ | :------------ | :--- | :---------- | :------- | :--------------------- |
| `BeeHeadlineLarge`  | `headlineLarge`  | Manrope | ExtraBold 800 | 32sp | —           | -0.25sp  | Page titles            |
| `BeeHeadlineMedium` | `headlineMedium` | Manrope | Bold 700      | 24sp | —           | 0sp      | Section titles         |
| `BeeHeadlineSmall`  | `headlineSmall`  | Manrope | SemiBold 600  | 18sp | —           | 0.15sp   | Card titles            |
| `BeeBodyLarge`      | `bodyLarge`      | Inter   | Regular 400   | 16sp | 24sp        | 0.5sp    | Long-form content      |
| `BeeBodyMedium`     | `bodyMedium`     | Inter   | Regular 400   | 14sp | 20sp        | 0.25sp   | Descriptions, captions |
| `BeeBodySmall`      | `bodySmall`      | Inter   | Medium 500    | 12sp | 16sp        | 0.4sp    | Labels, hints          |
| `BeeLabelLarge`     | `labelLarge`     | Inter   | Bold 700      | 16sp | 24sp        | 0.1sp    | Button text (primary)  |
| `BeeLabelMedium`    | `labelMedium`    | Inter   | SemiBold 600  | 14sp | 20sp        | 0.5sp    | Chip text              |
| `BeeLabelSmall`     | `labelSmall`     | Inter   | SemiBold 600  | 12sp | 16sp        | 0.5sp    | Small UI labels        |

**Rule:** body styles = content/reading; label styles = UI controls (buttons, chips, tabs). Default color for all `BeeText` variants is `onSurface` — always pass explicit `color` when inside Surface components with state-dependent `contentColor`.

---

## 3. Spacing

Object: `BeeSpacing`

| Token | dp  |
| :---- | :-- |
| `XS`  | 4   |
| `S`   | 8   |
| `M`   | 16  |
| `L`   | 24  |
| `XL`  | 32  |
| `XXL` | 48  |

Screen horizontal padding: `BeeSpacing.L` (24dp). Section gaps: `BeeSpacing.XL`–`BeeSpacing.XXL`.

---

## 4. Shapes

| Token  | Radius                 | Used by                                                         |
| :----- | :--------------------- | :-------------------------------------------------------------- |
| small  | 8dp                    | Small inputs, checkboxes                                        |
| medium | 12dp                   | Buttons (`BeeButtonShape`), cards (`BeeCardShape`), text inputs |
| large  | 24dp                   | Dialogs                                                         |
| xl     | 32dp                   | Section/interactive cards (`BeeCardLargeShape`)                 |
| full   | 9999dp / `CircleShape` | Chips, FAB, badges                                              |

---

## 5. Components

### Buttons — height 56dp, shape `radius-medium` (12dp)

| Composable           | Container            | Content                | Label composable                                                             |
| :------------------- | :------------------- | :--------------------- | :--------------------------------------------------------------------------- |
| `BeeButtonPrimary`   | `primary`            | `onPrimary`            | `BeeLabelLarge(..., color = MaterialTheme.colorScheme.onPrimary)`            |
| `BeeButtonSecondary` | `secondaryContainer` | `onSecondaryContainer` | `BeeLabelLarge(..., color = MaterialTheme.colorScheme.onSecondaryContainer)` |
| `BeeButtonOutlined`  | transparent          | `onSurface`            | `BeeLabelLarge(...)`                                                         |
| `BeeButtonGhost`     | transparent          | `primary`              | `BeeLabelLarge(..., color = MaterialTheme.colorScheme.primary)`              |
| `BeeFAB`             | `primaryContainer`   | `onPrimaryContainer`   | `Icon(...)`                                                                  |

### Cards

| Composable           | Container                | Elevation | Shape | Content color |
| :------------------- | :----------------------- | :-------- | :---- | :------------ |
| `BeeCardElevated`    | `surfaceContainerLowest` | 1dp       | 12dp  | `onPrimary`   |
| `BeeCardTonal`       | `surfaceContainerLow`    | 0dp       | 12dp  | `onPrimary`   |
| `BeeCardSection`     | `surfaceContainerLow`    | 0dp       | 32dp  | `onSurface`   |
| `BeeCardInteractive` | `surfaceContainerLowest` | 1dp       | 32dp  | `onSurface`   |
| `BeeCardOutlined`    | `surface`                | —         | 12dp  | `onSurface`   |

### Chips — shape `CircleShape`, padding h:16dp v:8dp

| Composable   | State      | Container                      | Text color                       | Composable                                  |
| :----------- | :--------- | :----------------------------- | :------------------------------- | :------------------------------------------ |
| `BeeChip`    | selected   | `primary`                      | `onPrimary`                      | `BeeLabelMedium(..., color = contentColor)` |
| `BeeChip`    | unselected | `secondaryContainer`           | `onSecondaryContainer`           | `BeeLabelMedium(..., color = contentColor)` |
| `BeeChipTag` | static     | `secondaryContainer` (default) | `onSecondaryContainer` (default) | `BeeLabelMedium(..., color = contentColor)` |

Activity chips → `secondaryContainer`. Emotion chips → `TertiaryContainer` / `OnTertiaryContainer`.

### Text Fields

| Composable     | Shape | Background               | Notes                  |
| :------------- | :---- | :----------------------- | :--------------------- |
| `BeeTextField` | 12dp  | `surfaceContainerLowest` | Single-line            |
| `BeeTextArea`  | 12dp  | `surfaceContainerLowest` | Multi-line, borderless |

---

## 6. Elevation & Depth

Depth through tonal layers, not shadows.

| Level        | Surface token            | Use                                |
| :----------- | :----------------------- | :--------------------------------- |
| 0 — Base     | `surface`                | Screen background                  |
| 1 — Sections | `surfaceContainerLow`    | Cards, section containers          |
| 2 — Items    | `surfaceContainerLowest` | Interactive / elevated white cards |

FABs / modals: `32px` blur shadow with `primary` tint (not black), 6–25% opacity.
Ghost border fallback (accessibility only): `outlineVariant` at 15% opacity.

---

## 7. Rules Summary

- **No hardcoded colors** in Compose — always use `MaterialTheme.colorScheme.*` or named theme tokens.
- **No 1px solid borders** for layout separation — use tonal background shifts.
- **Buttons always use label typography** (`BeeLabelLarge`), never body.
- **BeeText defaults to `onSurface`** — pass explicit `color` inside Surface with state-dependent contentColor (chips, cards with custom backgrounds).
- **`primaryContainer` (yellow) is never a button background** — reserved for highlights, chips, FAB.
- **Minimum touch target:** 48dp height.
