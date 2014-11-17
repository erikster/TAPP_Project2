rm src/*.class
javac -cp lib/lwjgl.jar:lib/slick.jar:src src/SlickSandbox.java
java  -cp lib/lwjgl.jar:lib/slick.jar:src -Djava.library.path=native SlickSandbox