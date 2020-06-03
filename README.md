# CurrencyApp

In this App, the main purpose is listing all currencies which are provided by the endpoint. 
The first item of the list represents the selected currency type and quantity. All the other 
currency rates are populated and calculated according to the selected item. 

<img src="https://github.com/tongtongi/CurrencyApp/blob/master/Gif.gif" width="350" height="700"/>

### Requirements
- Update rates in every 1 second.
- First item of the list is the base currency.
- Other rates in the list should be updated according to the given base currency value.
- App should survive on configuration change.

### Implementation Detail
- This app is implemented 100% in Kotlin.
- MVVM Architecture, LiveData, ViewModel from Architecture Components
- Coroutines 
- Retrofit for REST calls
- Koin for dependency injection

