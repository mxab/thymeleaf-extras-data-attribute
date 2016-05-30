thymeleaf-extras-data-attribute
===============================

Thymeleaf dialect for data attributes

# Setup
## Maven
    <dependency>
        <groupId>com.github.mxab.thymeleaf.extras</groupId>
        <artifactId>thymeleaf-extras-data-attribute</artifactId>
        <version>2.0.1</version>
    </dependency>

## Code
    templateEngine.addDialect(new DataAttributeDialect());

# Usage
## Source
    <html>
    <body data:foo="${'bar'}" data:msg="#{my.message}" >
    </body>
    </html>

## Result
    <html>
    <body data-foo="bar" data-msg="Your resolved message" >
    </body>
    </html>

# Credits

Big shout-out to @dtrunk90 who rewrote everything for Thymeleaf 3.0
