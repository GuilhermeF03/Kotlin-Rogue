# What are signals?

Signals are based on the [observer pattern](https://refactoring.guru/design-patterns/observer "‌"), 
where there are two types of components:
* Publisher: Publishes information
* Subscriber: Subscribes to events

## Why are they useful?
Imagine you have a inventory singleton that holds all player inventory data.

Now imagine you have a UI entity that displays the currently equipped weapon.

The UI entity could poll the singleton in order to check if any changes were made
and update its state accordingly, but this would be cumbersome as would need a 
while loop. Also, for every new entity that requires some state info would also need
an individual loop.

This proves un-maintainable long-term, so we can use a much
cleaner approach through the use of the **observer pattern**

### 1. Define the signal
Signals are basically holders of a list of callbacks with a specific signature,
so we just need to define the type of signal based on the number of parameters it accepts,
and the type of each parameter, like so:

````kotlin
val noArgSignal : NoArgSignal = createSignal()
val oneArgSignal : OneArgSignal<Int> = createSignal()
val twoArgsSignal : TwoArgsSignal<Int, Int> = createSignal()
````
### 2. Define the callback
Next we need to define a callback method.
````kotlin
val callback = { println("Event called") }
````
### 3. Subscribe to event
Now we can subscribe to this event, using the **connect** function
````kotlin
noArgSignal connect callback
// or
noArgSignal.connect(callback)
// you can also pass lambdas
noArgSignal connect {
    println("Lambda event called")
}
````
### 4. Emit event
Finally, we only need to emit data, and all the registered callbacks are called
````kotlin
noArgSignal.emit()
````
---
## Signal Variables
Now that we defined how we can use signals, let's dive into a specific use-case.

Suppose you have a counter inside the inventory:
````kotlin
var gold = 0 // holds current player gold
````
Imagine we need to update other entities, so we need to define an individual signal
````kotlin
val onGoldChange : OneArgSignal<Int> = createSignal()
````
Now everytime we update this counter we need to also emit the new value:
````kotlin
gold = newValue
onGoldChange.emit(newValue)
````
This can also prove un-maintainable of error-prone, in case someone forgets to define a signal or emit it.
For these reasons we have a cleaner approach.

**SignalVal**s are basically wrappers on the signalling system, and work similliar to signals on other frameworks,
such as **Angular**.

### 1. Define a SignalVal
For the gold counter example, now we define it as
````kotlin
val gold = SignalVal(0)
// or
val gold = 0.asSignalVal()
````
### 2. Connect to it
Subscribers can now connect to it like a normal signal
````kotlin
gold connect callback
````
### 3. Emit on value changed
Now we only need to update its value, like so:
````kotlin
gold.value = newValue // emits automatically
````
### 4.(Optional) use it concurrently
You can also use it with [flows](https://kotlinlang.org/docs/flow.html)
````kotlin
signalVal.flow.collect {
    println("Value: $it")
}
````
> Note: not really useful for our game, just fun






