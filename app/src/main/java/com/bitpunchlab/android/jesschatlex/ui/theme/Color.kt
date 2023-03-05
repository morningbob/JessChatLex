package com.bitpunchlab.android.jesschatlex.ui.theme

import androidx.compose.ui.graphics.Color
import com.bitpunchlab.android.jesschatlex.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex.helpers.Element

object JessChatLex {

    var bannerColor  = listOf(Color(0xFF2677e0), Color(0xFF0c7829), Color(0xFF826004), Color(0xFF570ad1),
        Color(0xFF032e73))
    var textColor = listOf(Color(0xFF0b29bd), Color(0xFF046b23), Color(0xFF704903), Color(0xFF500880),
        Color(0xFF94d0f2))
    var backgroundColor = listOf(Color(0xFFabe2ed), Color(0xFFb3f5c7), Color(0xFF826004),Color(0xFF570ad1),
        Color(0xFF032e73))
    var buttonColor = listOf(Color(0xFF2677e0), Color(0xFF0c7829), Color(0xFF826004), Color(0xFFbc9bfa),
        Color.Transparent)
    var buttonBorder = listOf(Color(0xFF2677e0), Color(0xFF0c7829), Color(0xFF826004), Color(0xFF570ad1),
        Color(0xFF94d0f2))
    var buttonText = listOf(Color.White, Color.White, Color.White, Color.White,
        Color(0xFF94d0f2))
    var buttonBackground = listOf(Color(0xFFabe2ed), Color(0xFFb3f5c7), Color(0xFFf0cd90), Color(0xFFbc9bfa),
        Color.Transparent)
    var clickableText = listOf(Color(0xFF2677e0), Color(0xFF0c7829), Color(0xFF826004), Color(0xFF570ad1),
        Color.Yellow)

    val lightBlue = HashMap<Element, Color>()
    val lightGreen = HashMap<Element, Color>()
    val lightBrown = HashMap<Element, Color>()
    val lightPurple = HashMap<Element, Color>()
    val darkBlue = HashMap<Element, Color>()
    val darkGreen = HashMap<Element, Color>()
    val darkBrown = HashMap<Element, Color>()
    val darkPurple = HashMap<Element, Color>()
    val colorScheme = HashMap<ColorMode, HashMap<Element, Color>>()

    init {
        setupColor()
    }

    fun setupColor() {

        lightBlue.put(Element.BANNER, Color(0xFF2677e0))
        lightBlue.put(Element.TEXT, Color(0xFF0b29bd))
        lightBlue.put(Element.BACKGROUND, Color(0xFFabe2ed))
        lightBlue.put(Element.BUTTON_COLOR, Color(0xFF2677e0))
        lightBlue.put(Element.BUTTON_BORDER, Color(0xFF2677e0))
        lightBlue.put(Element.BUTTON_TEXT, Color.White)
        lightBlue.put(Element.BUTTON_BACKGROUND, Color(0xFFabe2ed))
        lightBlue.put(Element.CLICKABLE, Color(0xFF2677e0))

        lightGreen.put(Element.BANNER, Color(0xFF0c7829))
        lightGreen.put(Element.TEXT, Color(0xFF046b23))
        lightGreen.put(Element.BACKGROUND, Color(0xFFb3f5c7))
        lightGreen.put(Element.BUTTON_COLOR, Color(0xFF0c7829))
        lightGreen.put(Element.BUTTON_BORDER, Color(0xFF0c7829))
        lightGreen.put(Element.BUTTON_TEXT, Color.White)
        lightGreen.put(Element.BUTTON_BACKGROUND, Color(0xFFb3f5c7))
        lightGreen.put(Element.CLICKABLE, Color(0xFF0c7829))

        lightBrown.put(Element.BANNER, Color(0xFF826004))
        lightBrown.put(Element.TEXT, Color(0xFF704903))
        lightBrown.put(Element.BACKGROUND, Color(0xFF826004))
        lightBrown.put(Element.BUTTON_COLOR, Color(0xFF826004))
        lightBrown.put(Element.BUTTON_BORDER, Color(0xFF826004))
        lightBrown.put(Element.BUTTON_TEXT, Color.White)
        lightBrown.put(Element.BUTTON_BACKGROUND, Color(0xFFf0cd90))
        lightBrown.put(Element.CLICKABLE, Color(0xFF826004))

        lightPurple.put(Element.BANNER, Color(0xFF570ad1))
        lightPurple.put(Element.TEXT, Color(0xFF500880))
        lightPurple.put(Element.BACKGROUND, Color(0xFF570ad1))
        lightPurple.put(Element.BUTTON_COLOR, Color(0xFFbc9bfa))
        lightPurple.put(Element.BUTTON_BORDER, Color(0xFF570ad1))
        lightPurple.put(Element.BUTTON_TEXT, Color.White)
        lightPurple.put(Element.BUTTON_BACKGROUND, Color(0xFFbc9bfa))
        lightPurple.put(Element.CLICKABLE, Color(0xFF570ad1))

        darkBlue.put(Element.BANNER, Color(0xFF032e73))
        darkBlue.put(Element.TEXT, Color(0xFF94d0f2))
        darkBlue.put(Element.BACKGROUND, Color(0xFF032e73))
        darkBlue.put(Element.BUTTON_COLOR, Color.Transparent)
        darkBlue.put(Element.BUTTON_BORDER, Color(0xFF94d0f2))
        darkBlue.put(Element.BUTTON_TEXT, Color(0xFF94d0f2))
        darkBlue.put(Element.BUTTON_BACKGROUND, Color.Transparent)
        darkBlue.put(Element.CLICKABLE, Color.Yellow)

        //
        darkGreen.put(Element.BANNER, Color(0xFF032e73))
        darkGreen.put(Element.TEXT, Color(0xFF94d0f2))
        darkGreen.put(Element.BACKGROUND, Color(0xFF032e73))
        darkGreen.put(Element.BUTTON_COLOR, Color.Transparent)
        darkGreen.put(Element.BUTTON_BORDER, Color(0xFF94d0f2))
        darkGreen.put(Element.BUTTON_TEXT, Color(0xFF94d0f2))
        darkGreen.put(Element.BUTTON_BACKGROUND, Color.Transparent)
        darkGreen.put(Element.CLICKABLE, Color.Yellow)

        darkBrown.put(Element.BANNER, Color(0xFF032e73))
        darkBrown.put(Element.TEXT, Color(0xFF94d0f2))
        darkBrown.put(Element.BACKGROUND, Color(0xFF032e73))
        darkBrown.put(Element.BUTTON_COLOR, Color.Transparent)
        darkBrown.put(Element.BUTTON_BORDER, Color(0xFF94d0f2))
        darkBrown.put(Element.BUTTON_TEXT, Color(0xFF94d0f2))
        darkBrown.put(Element.BUTTON_BACKGROUND, Color.Transparent)
        darkBrown.put(Element.CLICKABLE, Color.Yellow)

        darkPurple.put(Element.BANNER, Color(0xFF032e73))
        darkPurple.put(Element.TEXT, Color(0xFF94d0f2))
        darkPurple.put(Element.BACKGROUND, Color(0xFF032e73))
        darkPurple.put(Element.BUTTON_COLOR, Color.Transparent)
        darkPurple.put(Element.BUTTON_BORDER, Color(0xFF94d0f2))
        darkPurple.put(Element.BUTTON_TEXT, Color(0xFF94d0f2))
        darkPurple.put(Element.BUTTON_BACKGROUND, Color.Transparent)
        darkPurple.put(Element.CLICKABLE, Color.Yellow)


        colorScheme.put(ColorMode.LIGHT_BLUE, lightBlue)
        colorScheme.put(ColorMode.LIGHT_GREEN, lightGreen)
        colorScheme.put(ColorMode.LIGHT_BROWN, lightBrown)
        colorScheme.put(ColorMode.LIGHT_PURPLE, lightPurple)
        colorScheme.put(ColorMode.DARK_BLUE, darkBlue)
        colorScheme.put(ColorMode.DARK_GREEN, darkGreen)
        colorScheme.put(ColorMode.DARK_BROWN, darkBrown)
        colorScheme.put(ColorMode.DARK_PURPLE, darkPurple)
    }

    fun getColor(mode: ColorMode, element: Element) : Color {
        return colorScheme[mode]!![element]!!
    }

    val lightBlueBackground = Color(0xFFabe2ed)
    val blueBackground = Color(0xFF2677e0)
    val greenBackground = Color(0xFF0c7829)
    val brownBackground = Color(0xFF826004)
    val lightGreenBackground = Color(0xFFb3f5c7)
    val lightBrownBackground = Color(0xFFf0cd90)
    val lightPurpleBackground = Color(0xFFbc9bfa)
    val purpleBackground = Color(0xFF570ad1)
    val purpleText = Color(0xFF500880)
    val blueText = Color(0xFF0b29bd)
    val brownText = Color(0xFF704903)
    val disabledGreen = Color(0xFF208c3d)
    val titleColor = Color(0xFF041a87)
    //val contentColor = Color(0xFF0b29bd)
    val greenText = Color(0xFF046b23)
    val errorColor = Color.Red
    val textFieldBorderColor = Color(0xFF2677e0)
    val buttonBorderColor = Color(0xFF09b559)
    //val buttonTextColor = Color(0xFF2677e0)
    //val buttonTextColor = Color(0xFF09b559)
    val dialogBackgroundColor = Color(0xFFb6f0d4)
    val dialogMessageColor = Color(0xFF243bed)
    val dialogBlueBackground = Color(0xFF59b3f0)
    val dialogGreenBackgound = Color(0xFF6dde8f)

    val messageColorUser = Color(0xFF088a36)

    val darkBlueBackground = Color(0xFF032e73)
    val darkBlueText = Color(0xFF94d0f2)
}



