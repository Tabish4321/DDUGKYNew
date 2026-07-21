package com.deendayalproject.esop.exam

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(

    /* ───────────── Spacing (8dp scale) ───────────── */

    val space2XS: Dp = 4.dp,
    val spaceXS: Dp = 8.dp,
    val spaceS: Dp = 12.dp,
    val spaceM: Dp = 16.dp,
    val spaceL: Dp = 24.dp,
    val spaceXL: Dp = 32.dp,
    val space2XL: Dp = 48.dp,
    val space3XL: Dp = 60.dp,
    val space4XL: Dp = 80.dp,

    val homeBackgroundBox: Dp = 220.dp,
    val actionCardTotalSyncHeight: Dp = 180.dp,
    val bottomSheetTopShape: Dp = 20.dp,


    /* ───────────── Screen Padding ───────────── */

    val screenPaddingHorizontal: Dp = 16.dp,
    val screenPaddingVertical: Dp = 16.dp,

    /* ───────────── Heights / Touch ───────────── */

    val toolbarHeight: Dp = 56.dp,
    val toolbarPadding: Dp = 16.dp,
    val toolbarEndIconHeight: Dp = 36.dp,
    val toolbarEndIconWidth: Dp = 72.dp,
    val buttonHeight: Dp = 52.dp,
    val textFieldHeight: Dp = 56.dp,
    val minTouchTarget: Dp = 48.dp,

    /* ───────────── Icons / Images ───────────── */

    val iconXS: Dp = 18.dp,
    val iconS: Dp = 24.dp,
    val iconM: Dp = 28.dp,
    val iconL: Dp = 40.dp,
    val iconXL: Dp = 56.dp,

    val avatarS: Dp = 48.dp,
    val avatarM: Dp = 64.dp,
    val avatarL: Dp = 72.dp,

    val logoHeight: Dp = 80.dp,
    val logoWidth: Dp = 120.dp,

    /* ───────────── Shape / Radius ───────────── */

    val radiusS: Dp = 8.dp,
    val radiusM: Dp = 12.dp,
    val radiusL: Dp = 20.dp,
    val radiusXL: Dp = 20.dp,

    /* ───────────── Elevation ───────────── */

    val elevationS: Dp = 2.dp,
    val elevationM: Dp = 4.dp,
    val elevationL: Dp = 6.dp,
    val elevationXL: Dp = 8.dp
)



// COMPACT SMALL (≤360dp width)
// Example: Xiaomi Mi A1, older phones

val CompactSmallDimens = Dimens(
    spaceXS = 6.dp,
    spaceS = 10.dp,
    spaceM = 12.dp,
    spaceL = 16.dp,
    spaceXL = 24.dp,
    space2XL = 36.dp,
    space3XL = 48.dp,
    space4XL = 64.dp,

    toolbarHeight = 52.dp,
    toolbarPadding = 12.dp,
    toolbarEndIconHeight = 36.dp,
    toolbarEndIconWidth = 66.dp,
    homeBackgroundBox = 180.dp,
    actionCardTotalSyncHeight = 160.dp,
    bottomSheetTopShape = 16.dp,

    screenPaddingHorizontal = 12.dp,
    screenPaddingVertical = 12.dp,

    radiusM = 8.dp,
    radiusL = 16.dp,
    radiusXL = 16.dp,

    buttonHeight = 48.dp,
    logoHeight = 64.dp,
    logoWidth = 100.dp,

    iconXS = 16.dp,
    iconS = 20.dp,
    iconM = 24.dp,
    iconXL = 48.dp,
    avatarS = 32.dp,
    avatarM = 48.dp
)


// 📱 COMPACT (normal phones – DEFAULT)
// Example: Pixel, Samsung, Motorola

val CompactDimens = Dimens()


// 📱 MEDIUM (fold / large phones / small tablets)
// Example: Fold inner, 7–8 inch tablets

val MediumDimens = Dimens(
    spaceXS = 10.dp,
    spaceS = 14.dp,
    spaceM = 20.dp,
    spaceL = 28.dp,
    spaceXL = 40.dp,
    space2XL = 56.dp,
    space3XL = 68.dp,
    space4XL = 88.dp,

    toolbarHeight = 60.dp,
    toolbarPadding = 20.dp,
    toolbarEndIconHeight = 40.dp,
    toolbarEndIconWidth = 80.dp,
    homeBackgroundBox = 260.dp,
    actionCardTotalSyncHeight = 240.dp,
    bottomSheetTopShape = 24.dp,

    radiusM = 14.dp,
    radiusL = 20.dp,
    radiusXL = 24.dp,

    screenPaddingHorizontal = 24.dp,
    screenPaddingVertical = 20.dp,

    logoHeight = 96.dp,
    logoWidth = 144.dp,
    iconXS = 20.dp,
    iconS = 28.dp,
    iconM = 32.dp,
    iconXL = 64.dp,
    avatarS = 56.dp,
    avatarM = 72.dp,
    avatarL = 80.dp
)


// 📱 EXPANDED (tablets / large screens)
// Example: 10–12 inch tablets

val ExpandedDimens = Dimens(
    spaceXS = 12.dp,
    spaceS = 16.dp,
    spaceM = 24.dp,
    spaceL = 32.dp,
    spaceXL = 48.dp,
    space2XL = 64.dp,
    space3XL = 80.dp,
    space4XL = 100.dp,

    toolbarHeight = 64.dp,
    toolbarPadding = 24.dp,
    toolbarEndIconHeight = 44.dp,
    toolbarEndIconWidth = 96.dp,
    homeBackgroundBox = 300.dp,
    actionCardTotalSyncHeight = 280.dp,
    bottomSheetTopShape = 28.dp,

    radiusM = 16.dp,
    radiusL = 24.dp,
    radiusXL = 28.dp,

    screenPaddingHorizontal = 32.dp,
    screenPaddingVertical = 24.dp,

    logoHeight = 112.dp,
    logoWidth = 168.dp,
    iconXS = 24.dp,
    iconS = 32.dp,
    iconM = 32.dp,
    iconXL = 72.dp,
    avatarS = 72.dp,
    avatarM = 88.dp,
    avatarL = 96.dp,

    buttonHeight = 60.dp
)
