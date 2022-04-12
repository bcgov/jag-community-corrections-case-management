---
description: Iconography uses images and symbols to represent an object visually. They communicate a message and should be distinct and informative.
title: Icons
status: draft
---

![Status](https://img.shields.io/badge/Recommended-Draft-orange.svg)
> Last Updated: July 28, 2020

# Icons
Iconography uses images and symbols to represent an idea visually. They communicate a message and should be distinct and informative.

## Implementation
The design system supports using the Font Awesome icon library. Use these icons in your application by:
*	Hosting the [font library locally](https://fontawesome.com/how-to-use/on-the-web/setup/hosting-font-awesome-yourself) in your project source files
*	Using npm if you are using [Vue](https://fontawesome.com/how-to-use/on-the-web/using-with/vuejs), [Angular](https://fontawesome.com/how-to-use/on-the-web/using-with/angular), or [React](https://fontawesome.com/how-to-use/on-the-web/using-with/react) components

| Icon      | Label | Class Name |
| ----------- | ----------- | ----------- |
| ![magnifying glass](./icon-pngs/search.png) | Search      | `<i class="fas fa-search"></i>`|
| ![bars](./icon-pngs/bars-solid.png) | Menu Bars      | `<i class="fas fa-bars"></i>`|
| ![envelope](./icon-pngs/envelope-solid.png) | Envelope      | `<i class="fas fa-envelope"></i>`|
| ![phone](./icon-pngs/phone-solid.png) | Phone      | `<i class="fas fa-phone"></i>`|
| ![external link](./icon-pngs/external-link-alt-solid.png) | External Link      | `<i class="fas fa-external-link-alt"></i>`|
| ![Upload](./icon-pngs/upload-solid.png) | Upload      | `<i class="fas fa-upload"></i>`|
| ![up arrow](./icon-pngs/chevron-up-solid.png) | Up Arrow      | `<i class="fas fa-chevron-up"></i>`|
| ![right arrow](./icon-pngs/chevron-right-solid.png) | Right Arrow      | `<i class="fas fa-chevron-right"></i>`|
| ![down arrow](./icon-pngs/chevron-down-solid.png) | Down Arrow      | `<i class="fas fa-chevron-down"></i>`|
| ![left arrow](./icon-pngs/chevron-left-solid.png) | Left Arrow      | `<i class="fas fa-chevron-left"></i>`|

## Design Guidance
*	Icons should be used sparingly throughout the application to provide clarity and reduce cognitive load on users.
*	Icons do not have globally recognized meaning, and will vary based on people’s past experiences. Always test the icons you use to make sure they are understandable
*	Provide a text label with icons to clarify meaning
*	Do not provide a text label when the icon is decorative
*	Use solid icons rather than outline icons

Read more about [Icon Usability - Nielsen Norman Group](https://www.nngroup.com/articles/icon-usability/)

## Accessibility

### Screenreaders
* If your icon has a text label you should hide the icon from screen readers by using the `aria-hidden="true"` attribute

* If your icon doesn’t have a text label, you’ll need to manually add a few things so that your icon is appropriately accessible
  * Hide the text label from screen readers by using the `aria-hidden="true"` attribute
  *	Provide a text alternative inside a <span> (or similar) element. Also include appropriate CSS to visually hide the element while keeping it accessible to assistive technologies.
  *	Include a title attribute on the icon to provide a tooltip for sighted mouse users.

Read more about [Font Awesome Accessibility](https://fontawesome.com/how-to-use/on-the-web/other-topics/accessibility)

### Icon Size
The [click or tap area](https://www.w3.org/WAI/WCAG21/quickref/#target-size) around an icon should be a minimum of 44px by 44px (WCAG 2.1 AAA)

![Three icons that visually show a target area of 44px surrounding the icon](target-area.png)

### Colour Contrast
* Icons must have a 3:1 [color contrast ratio](https://www.w3.org/WAI/WCAG21/quickref/#non-text-contrast) (WCAG 2.1 AA) with the background colour.

![Two versions of the same icon with different contrast ratios](icon-contrast.png)

## Prototyping with Font Awesome Icons
Download the [Font Awesome Icons](https://fontawesome.com/how-to-use/on-the-desktop/setup/getting-started) for your desktop to use in your mockups and prototypes.
