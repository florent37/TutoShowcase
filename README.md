# Tuto Showcase

A simple and Elegant Showcase view for Android

[![screen](https://raw.githubusercontent.com/florent37/TutoShowCase/master/media/sample_small.png)](https://github.com/florent37/TutoShowcase)

```java
Tuto.from(this)
    .setContentView(R.layout.tuto_sample)

    .on(R.id.about) //a view in actionbar
    .addCircle()
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

...

# Indicators

You can higlight some elements to user

## Circle

```java
.on(view)
.addCircle()
```

## RoundRect

```java
.on(view)
.addRoundRect()
```

# Actions

Some actions can be explained to the user

## Scrollable

```java
.on(view)
.displayScrollable()
```

## Swipable Left / Right

```java
.on(view)
.displaySwipableLeft()

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