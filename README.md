# Tuto Showcase

A simple and Elegant Showcase view for Android

[![screen](https://raw.githubusercontent.com/florent37/TutoShowcase/master/media/sample.png)](https://github.com/florent37/TutoShowcase)

```java
TutoShowcase.from(this)
    .setContentView(R.layout.tuto_sample)

    .on(R.id.about) //a view in actionbar
    .addCircle()
    .withBorder()
    .onClick(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //custom action
        }
    })

    .on(R.id.swipable)
    .displaySwipableRight()

    .show();
```

# Tutorial

You can simply limit a showcase visibility to once with `.showOnce(string)`

# Content View

It's simple to add a content view into the TutoShowcase,
you can for example add images or descriptions texts

```java
TutoShowcase.from(this)
    .setContentView(R.layout.tuto_sample)
    ...
    .show()
```

[![screen](https://raw.githubusercontent.com/florent37/TutoShowcase/master/media/content.png)](https://github.com/florent37/TutoShowcase)

# Indicators

You can higlight some elements to user

## Circle

```java
.on(view)
.addCircle()
```

[![screen](https://raw.githubusercontent.com/florent37/TutoShowcase/master/media/circle.png)](https://github.com/florent37/TutoShowcase)

## RoundRect

```java
.on(view)
.addRoundRect()
```

[![screen](https://raw.githubusercontent.com/florent37/TutoShowcase/master/media/roundrect.png)](https://github.com/florent37/TutoShowcase)

# Actions

Some actions can be explained to the user

## Scrollable

```java
.on(view)
.displayScrollable()
```

## Swipable Left

[![screen](https://raw.githubusercontent.com/florent37/TutoShowcase/master/media/swipeLeft.gif)](https://github.com/florent37/TutoShowcase)

```java
.on(view)
.displaySwipableLeft()
```

## Swipable Right

[![screen](https://raw.githubusercontent.com/florent37/TutoShowcase/master/media/swipeRight.gif)](https://github.com/florent37/TutoShowcase)
```java
.on(view)
.displaySwipableRight()
```

# Events

You can listen for indicator click

```java
.on(view)
. //your indicator
.onClick(new View.OnClickListener(){
    public void onClick(View view){
         //your action
    }
}
```

If you have any clickable view into your content layout 

```java
TutoShowcase.from(this)
    .setContentView(R.layout.tuto_sample)
    .onClick(R.id.clickableView, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            
                        }
                    })
    ...
    .show()
```