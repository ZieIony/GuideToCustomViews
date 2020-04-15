# GuideToCustomViews
The ultimate guide to Android custom views

I've been designing, writing and publishing Android custom views for the past 5 years now. I guess it's about time to sum it up and share the results.

Here you will find the topics related to custom views and a couple of case studies. Code samples will be in Java and Kotlin randomly and the code will be as simple as possible to demonstrate ideas and techniques better. I want this place to be more of a tutorial wiki than a documentation. You can find the documentation on [developer.android.com/](https://developer.android.com/).

I'll try to add an article or two per week. If there's something missing and you think that I should write about - raise an issue and tell me. If you wish, there's a larger repository with my custom views [here](https://github.com/ZieIony/Carbon).

### Topics

1. [Basics](https://github.com/ZieIony/GuideToCustomViews/wiki/Basics)
1. [What to extend](https://github.com/ZieIony/GuideToCustomViews/wiki/What-to-extend)
1. [Prefix everything](https://github.com/ZieIony/GuideToCustomViews/wiki/Prefix-everything)

##### Code

1. [Constructors](https://github.com/ZieIony/GuideToCustomViews/wiki/Constructors)
1. [Measuring](https://github.com/ZieIony/GuideToCustomViews/wiki/Measuring)
1. [Custom states](https://github.com/ZieIony/GuideToCustomViews/wiki/Custom-states)
1. [State saving](https://github.com/ZieIony/GuideToCustomViews/wiki/State-saving)
1. [Edit mode](https://github.com/ZieIony/GuideToCustomViews/wiki/Edit-mode)
1. [Drawables](https://github.com/ZieIony/GuideToCustomViews/wiki/Drawables)

##### Attributes and styles

1. [Using attributes](https://github.com/ZieIony/GuideToCustomViews/wiki/Using-attributes)
1. [Custom attributes](https://github.com/ZieIony/GuideToCustomViews/wiki/Custom-attributes)
1. [Custom layout attributes](https://github.com/ZieIony/GuideToCustomViews/wiki/Custom-layout-attributes)

##### Accessibility

1. [Basic accessibility](https://github.com/ZieIony/GuideToCustomViews/wiki/Basic-accessibility)
1. [Content description and events](https://github.com/ZieIony/GuideToCustomViews/wiki/Content-description-and-events)

### Case studies

##### ProgressTextView

<img src="https://github.com/ZieIony/GuideToCustomViews/blob/master/progresstextview/result.png" width="25%" height="25%"/>

A progress bar that also has a text. The text should be drawn in one color on the 'done' part of the progress and in another color on the 'remaining' part.

See: [guide](https://github.com/ZieIony/GuideToCustomViews/wiki/ProgressTextView), [code](https://github.com/ZieIony/GuideToCustomViews/tree/master/progresstextview).

##### ChartView

<img src="https://github.com/ZieIony/GuideToCustomViews/blob/master/images/chartview.png" width="25%" height="25%"/>

A simple bar chart with color state lists and bar selecting with clicks.

Topics covered: drawing on canvas, state lists, touch events.

See: [code](https://github.com/ZieIony/GuideToCustomViews/tree/master/chartview).

A more complete version of the view can be found [here](https://github.com/ZieIony/Carbon/blob/master/carbon/src/main/java/carbon/beta/ChartView.java)

##### FlowLayout

<img src="https://github.com/ZieIony/GuideToCustomViews/blob/master/images/flowlayout.png" width="25%" height="25%"/>

A layout that displays its children in rows, side to side and then in another line.

Topics covered: measuring, laying out, custom layout attributes, right to left support.

See: [code](https://github.com/ZieIony/GuideToCustomViews/tree/master/flowlayout).

##### InvalidEditText

An EditText with support for an invalid state attribute.

Topics covered: custom states.

See: [guide](https://github.com/ZieIony/GuideToCustomViews/wiki/InvalidEditText), [code](https://github.com/ZieIony/GuideToCustomViews/tree/master/invalidedittext).

##### MoodToggle

<img src="https://github.com/ZieIony/GuideToCustomViews/blob/master/images/moodtoggle.png" width="25%" height="25%"/>

A very simple toggle with accessibility support.

Topics covered: basic accessibility.

See: [guide](https://github.com/ZieIony/GuideToCustomViews/wiki/MoodToggle), [code](https://github.com/ZieIony/GuideToCustomViews/tree/master/moodtoggle).
