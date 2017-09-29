# Tuto Showcase

A simple and Elegant Showcase view for Android

<a href="https://play.google.com/store/apps/details?id=com.github.florent37.florent.champigny">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

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

# Download

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

[ ![Download](https://api.bintray.com/packages/florent37/maven/TutoShowcase/images/download.svg) ](https://bintray.com/florent37/maven/TutoShowcase/_latestVersion)

```groovy
compile 'com.github.florent37:tutoshowcase:1.0.1'
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

```java
.on(view)
.displaySwipableLeft()
```

[![screen](https://raw.githubusercontent.com/florent37/TutoShowcase/master/media/swipeLeft.gif)](https://github.com/florent37/TutoShowcase)

## Swipable Right

```java
.on(view)
.displaySwipableRight()
```

[![screen](https://raw.githubusercontent.com/florent37/TutoShowcase/master/media/swipeRight.gif)](https://github.com/florent37/TutoShowcase)

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
    .onClickContentView(R.id.clickableView, new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                            
        }
    })
    ...
    .show()
```

<a href="https://play.google.com/store/apps/details?id=com.github.florent37.florent.champigny">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>
