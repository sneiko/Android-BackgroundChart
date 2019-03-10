[![](https://jitpack.io/v/JastAir/Android-BackgroundChart.svg)](https://jitpack.io/#JastAir/Android-BackgroundChart)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Background%20Chart-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7561)

# Android-BackgroundChart

![screenshot](https://github.com/JastAir/Android-BackgroundChart/raw/master/%D0%A1%D0%BD%D0%B8%D0%BC%D0%BE%D0%BA%20%D1%8D%D0%BA%D1%80%D0%B0%D0%BD%D0%B0%202019-03-08%20%D0%B2%2019.32.03.png)



It's are simple chart for background your activity or fragment. You can choose colors with gradient for display your chart the most customize or choose what kind you chart need is now, simple lines or bezier.
Best it usage and good luck ;)


# How use

    <com.fdev.backgroundchart.GradientChart  
      android:id="@+id/gradientChart"  
      android:layout_width="0dp"  
      android:layout_height="0dp"  
      app:layout_constraintBottom_toBottomOf="parent"  
      app:layout_constraintEnd_toEndOf="parent"  
      app:layout_constraintStart_toStartOf="parent"  
      app:layout_constraintTop_toTopOf="parent"  
      app:plusColorStart="#2196F3"  
      app:plusColorEnd="#90CAF9"  
      app:minusColorStart="#90CAF9"  
      app:minusColorEnd="#90CAF9"  
      app:zoom="10"  
      app:isBezier="true"/>
      
**And for appent your data just write for example** 

    gradientChart.chartValues = arrayOf(  
      10f, 30f, 25f, 32f, 13f, 5f, 18f, 36f, 20f, 30f, 28f, 27f, 29f  
    )

# Gradle

**Step 1.**  Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

**Step 2.**  Add the dependency

```
	dependencies {
	        implementation 'com.github.JastAir:Android-BackgroundChart:{version}'
	}

```
