package com.ujjolch.masterapp

import androidx.annotation.DrawableRes
import com.example.masterapp.R

sealed class Screen(val title:String,val route:String){
    sealed class BottomScreen(val btitle:String,val broute:String,@DrawableRes val icon:Int):Screen(btitle,broute){
        object HomeScreen:BottomScreen("Home","Home", R.drawable.baseline_home_24)
        object HistoryScreen:BottomScreen("History","History",R.drawable.baseline_auto_graph_24)
        object MeScreen:BottomScreen("Me","Me",R.drawable.baseline_manage_accounts_24)
    }
    object IntroScreen:Screen("Intro Screen","introscreen")
    object SignInScreen:Screen("Sign In Screen","signin")
    object LogInScreen:Screen("Log In Screen","login")
    object ForgetPasswordScreen:Screen("forget password","forgetpasswordscreen")
    object AddDeviceScreen:Screen("Add Device Screen","adddevice")
    object UpdateDetailScreen:Screen("Update Detail Screen","updatedetail")
    object DOBScreen:Screen("your age","dobscreenfordetails")
    object HeightScreen:Screen("your height","heightscreenfordetails")
    object GenderScreen:Screen("your gender","genderscreenfordetails")
    object MainViewForUpdateDetails:Screen("MainView","mainviewforupdatedetails")
    object MainView:Screen("MainView","mainview")
    object BottomScreenAddDevice:Screen("Add Device","bottomadddevice")
    object BottomScreenUpdateDetails:Screen("Update Details","bottomscreenupdatedetails")
    object HealthReportScreen:Screen("Health Report","healthreport")
    object  ChangeUserScreen:Screen("Change User","bottomscreenchangeuser")
    object  AddUserScreen:Screen("Add User","bottomscreenadduser")
    object  UserManagementScreen:Screen("User Management","usermanagementscreen")
    object  SelectLanguageScreen:Screen("Languages","selectyourlanguage")
    object  AddDeviceCheckerScreen:Screen("Check Phone Status","adddevicecheckerscreen")
    object  NewAddDeviceScreen:Screen("Add Device","newadddevicescreen")
    object  AddDeviceNewNavigation:Screen("Add Device","newadddevicenavigation")
    object  VerifiyEmailScreen:Screen("Verify Email","verifyemailscreen")
    object  PrivacyPolicyScreen:Screen("Privacy Policy","privacypolicyscreen")
    object AIAssistantScreen:Screen("AI Assistant","chatbotscreen")


}
sealed class ScreenInMeScreen(val atitle:String,val aroute:String,@DrawableRes val icon:Int):Screen(atitle, aroute){
    object MyDevices:ScreenInMeScreen("My Devices","mydevices",R.drawable.baseline_monitor_weight_24)
    object UpdateDetails:ScreenInMeScreen("Update Details","updatedetails",R.drawable.baseline_update_24)
    object ChangePassword:ScreenInMeScreen("Change Password","changepassword",R.drawable.baseline_password_24)
    object Unit:ScreenInMeScreen("Unit","unitscreen",R.drawable.kg_weight)
    object UserManagement:ScreenInMeScreen("User Management","bottomscreenusermanagement",R.drawable.usermanagement)
    object SelectLanguageScreen:ScreenInMeScreen("Languages","bottomscreenselectlanguage",R.drawable.baseline_language_24)
}



val listofbottomitems = listOf(Screen.BottomScreen.HomeScreen,
    Screen.BottomScreen.HistoryScreen,
    Screen.BottomScreen.MeScreen)

//val listofmescreenitems = listOf(ScreenInMeScreen.MyDevices,
//    ScreenInMeScreen.UpdateDetails)