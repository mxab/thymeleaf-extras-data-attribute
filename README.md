thymeleaf-extras-data-attribute
===============================

Thymeleaf dialect for data attributes

#Setup
templateEngine.addDialect(new DataAttributeDialect());

#Usage
    <html>
    <body data:foo="${'bar'}" data:msg="#{my.message}" >
    </body>
    </html>
