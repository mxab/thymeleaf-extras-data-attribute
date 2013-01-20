thymeleaf-extras-data-attribute
===============================

Thymeleaf dialect for data attributes

#Setup
    templateEngine.addDialect(new DataAttributeDialect());

#Usage
##Source
    <html>
    <body data:foo="${'bar'}" data:msg="#{my.message}" >
    </body>
    </html>
    
##Result
    <html>
    <body data-foo="bar" data-msg="Your resolved message" >
    </body>
    </html>
