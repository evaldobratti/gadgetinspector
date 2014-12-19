Gadget Inspector

The purpose of this project is to help you debugging and analysing your objects in runtime, even if you haven't lanched your applicatin on debug mode on your IDE, and even if you're using an already generated build of your app.
You will inspect and execute some codes, inspect the states of objects, values of their fields, relationships, call their methods, execute queries, update databases, everything that you could do programmatically in your app.

You will use some windows to interact with the inspector, inspecting and executing a piece of code, that can be written, currently, in groovy and python languages. You can use all power of this languages in your code, lambdas, closures, list comprehensions, declare python and groovy classes, functions, everything that languages provides you.

CTRL + E executes your code
CTRL + I inspects your code

The project is designed to help you to program your own windows, but it provides you ways to just use as it is.
Just call

new GroovyInspector().start();

for Groovy Inspector, or

new JythonInspector().start();

for Python Inspector version.

The inspector will try to solve the imports that your code may use, so, you don't have to explicitly declare imports statements, inspector will search in the classpath, and if only one class with the required name is found, it will solve this dependency for you.