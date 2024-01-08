**DISCLAIMER:** This is not an official Google product.  Google's production dependency injection frameworks are [Dagger](https://github.com/google/dagger) and [Guice](https://github.com/google/guice).

# Tiger - The fastest java dependency injection framework

## Acknowledge
Tiger is highly inspired by Dagger, Kudos to those great guys.

## Challenge
If you find a faster DI framework, let me know. I will drop the title as long as it is proved.

## Why Tiger?
It is the fastest! Not faster, but the fastest! I have tried it on a big project with ~200 modules. While it takes hundreds of milliseconds to instantiate dagger components, on the same hardware, it only takes a few milliseconds to instantiate Tiger injectors.
Minimal amount of code to write therefore easy to maintain, if you need to maintain it at all. You don't need to write components like in Dagger. You don't need to split a module into several modules one for each scope that used by bindings provided by the module. You will feel it is some easy to change the scope of a binding. Just change it. Way to go, isn’t it?

## Build up your knowledge
If you are here, you must already be familiar with DI(Dependency Injection) and its advantage. Otherwise [wiki](https://en.wikipedia.org/wiki/Dependency_injection) will be your friend.
DI has been evolving for long time in the different form. But the concept is not really changed much. This document will not repeat these concepts. If you find some concept not explained, google it. Also Guice probably has explained those concept very well.

## Integration
Tiger is an annotation processor. Therefore, just build the jar and use it the way that annotation processors are 
supposed to be used. All environment should work. The sample is ready to be built by both gradle and maven.

## How?
Before diving into details, it will be helpful, very helpful, to understand the intended usage of tiger. Tiger is designed to let the machine do as much as possible and let human do as little as possible. It requires minimal information from the developer. From these information, scoped injectors are generated. Application can instances these injectors and use them to inject classes. To achieve this, tiger has distilled the information needed to generate injectors. Here are they, with related annotation.

## Scopes
Usually application has at least one scope, singleton. Even if there is no scoped binding, it is harmless to have a singleton scope. Therefore, tiger requires there always be a scope tree. The root is usually singleton, but not necessary. Details will be shown later in the sample. Tiger generates one injector class for each scope.

### `@tiger.ScopeDependency`
It specifies the dependencies between scopes. All the dependency information form a scope tree.

### `@dagger.Module`
It provides binding information through @Provides annotated methods with optional scope. Now(2016/08/10)  we just 
reuse dagger.Module. In the future, dagger.Module will be changed into tiger.Module so that tiger does not need to 
depend on dagger.

### `@javax.inject.Inject` on ctor
It provides binding information with optional scope.

### `@tiger.MembersInjector` with mandatory scope
It specifies interfaces which includes class that has fields or methods to be injected. Injectors will implement all these interfaces.

### `@javax.inject.Inject` on fields, methods
It specifies the injection points from which injector fans out when injecting an object.

### `@tiger.PackageForGenerated`
The package for the generated injectors.

### `@tiger.ScopedComponentNames`
It specifies the names of the injectors.

### `@tiger.GenerationTriggerAnnotation`
This the annotation that triggers the generation processor.
`@Module`, `@Inject` and `@MembersInjector` are naturally scattered around the code. For the others, i.e., `@ScopeDependency`, `@PackageForGenerated`, `@ScopedComponentNames` and `@GenerationTriggerAnnotation`, we suggest to put them into a dedicated java file as the central configuration information for the app.

Here is the depicted code the sample(with some modification)

``` java
@GenerationTriggerAnnotation
@PackageForGenerated("sample")
public class Scopes {

    @ScopedComponentNames(scope = Singleton.class, name = "ApplicationInjector" )
    public static class ForApplication {}

    @ScopedComponentNames(scope = sample.ActivityScoped.class, name = "ActivityInjector" )
    @ScopeDependency(scope = sample.ActivityScoped.class, parent = Singleton.class)
    public static class ForActivityScoped {}
}
```

Here is how ApplicationInjector is instantiated and used.

``` java
ApplicationInjector applicationInjector = new ApplicationInjector.Builder().build();
PseudoApplication application = new PseudoApplication();
applicationInjector.injectPseudoApplication(application);
```

Here is how ActivityInjector is instantiated and used.

``` java
ActivityInjector activityInjector = new ActivityInjector.Builder()
                                                        .applicationInjector(applicationInjector)
                                                        .build();
activityInjector.injectPseudoActivity(this);
```


The injectors guarantee that scoped bindings will be instantiated at most once within a scope. The application needs to create related injectors for scope objects, e.g., in android, a context scoped injector for each Activity, a singleton scoped injector for the Application.

## For Dagger users
As you can see, Tiger reuses annotations from dagger like dagger.Module, dagger.Provides, etc. You can find the javadoc [here](http://google.github.io/dagger/api/2.0/). We are not going to repeat them here. Of course, Component and Subcomponent is not needed any longer. Producer related stuff is also irrelevant to injection. There is one nice feature from Tiger. You don't need to split modules according to different scopes. Yes, you can put bindings of different scopes into one module. This way you have less modules. And, if you want to change the scope of a binding, just change it, easy.

## Tip
Inspecting the generate code will help you. If you want more, there is source code.
Enjoy injection!

## Group
[fastesttiger@googlegroups.com](https://groups.google.com/forum/?hl=en#!forum/fastesttiger)
