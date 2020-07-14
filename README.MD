# Photo Calculator Android App
## Description

Through this Android application, we can capture photos consisting simple Mathematics expression and then the result is auto-generated and displayed. We can perform addition, subtraction, multiplication and division on two operands. All the successful executed expressions are stored in disc and can be displayed from **History tab.**

## Generated APK

Navigate to [/APK](https://github.com/SayantanBanerjee16/PhotoCalculator/tree/master/APK) folder to get the generated APK.

## Final Product

* The base of the application looks like:-

 <img src="screenshots/1.jpg" width="200" height ="400">

* These are the respective outputs of addition, subtraction, multiplication, division.

 <img src="screenshots/2.jpg" width="200" height ="400"> <img src="screenshots/3.jpg" width="200" height ="400">
 <img src="screenshots/4.jpg" width="200" height ="400"> <img src="screenshots/5.jpg" width="200" height ="400">

* The history tab displays all of the latest calculated expressions.

 <img src="screenshots/6.jpg" width="200" height ="400">

* Ofcourse the border test-cases are taken into account. Any wrong expressions are not executed and instead an **Error** message is shown.

 <img src="screenshots/7.jpg" width="200" height ="400"> <img src="screenshots/8.jpg" width="200" height ="400">

## Concept used

 - Used [Kotlin](https://kotlinlang.org/) as the base language for this Android Application.

 - Used [Firebase MLKit Text Recognization API](https://firebase.google.com/docs/ml-kit/android/recognize-text) to recognize text from image which gets stored in a **String** which is executed through an Algorithm.

 - For the **History tab**, I have used [Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room) to store all the successful executed expressions onto the disc.