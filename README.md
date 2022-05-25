# JMatrix: proyecto de programac

## Disclaimer
Debido a la falta de tiempo este proyecto está bastante incompleto (habré hecho un 10% de las cosas que quería hacer) y tiene una cantidad exagerada de bugs que tienen que ver con NullPointerExceptions y con operaciones con números de precisión infinita de la biblioteca Apfloat.
Si quieres te puedo mandar la implementación de estos algoritmos que hice en C previamente para que veas que en la gran mayoría de casos es más una cosa de cómo funciona Java que de que los algoritmos de por sí estén mal, por si te interesa probarlos. Están hechos con la ayuda de GMP, MPFR, MPC y libquadmath.

También te hará falta habilitar los records.

Además, no he cumplido lo de los mapas y ArrayList en la clase contenedora porque no entiendo qué utilidad pueden tener en mi proyecto, así que como no sabía meterlos no los he metido.

Siento haber entregado un proyecto tan tarde y con tan mala calidad pero es que Java (sobre todo Swing) me desespera y ya llega un punto en el que no puedo arreglar tantos fallos que en muchos casos ya no dependen tanto de mí sino del lenguaje que estoy usando.

## Manual de uso
Para ejecutar el programa debes ejecutar el archivo MainWindow.java que se encuentra en el paquete org.jmatrix.graphical de la carpeta src.

El funcionamiento del programa es muy simple: tiene dos botones, uno que sirve para cargar una matriz serializada en un archivo y otro que sirve para generar una matriz.

Cargar una matriz no tiene mucho misterio, se pulsa el botón y se carga el archivo deseado.

Pulsar el botón de generar una matriz le abre al usuario un diálogo que le pide que seleccione el tipo de matriz a generar y al seleccionar uno de esos tipos de matriz te preguntará por propiedades.

Después de seleccionar el tipo de matriz a generar te preguntará si quieres guardarla en un archivo.

Después de eso te imprimirá una lista de las propiedades de la matriz, además de la propia matriz. Puedes cambiar cómo se ve la matriz yendo a la ventana principal y pulsando el botón "Style" del menú "Settings".

## Diagrama de clases
Puedes consultar el diagrama de clases en este mismo repositorio.
