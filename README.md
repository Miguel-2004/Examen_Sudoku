# SudokuApp

### Nombre: **Miguel Angel Becerra Ayala**  
### Matrícula: **A017100769**  
### Plataforma: **Android (Kotlin + Jetpack Compose)**

---

## Descripción del Proyecto
SudokuApp es una aplicación móvil desarrollada como parte del Examen Argumentativo Práctico de Desarrollo Móvil (TC2007B).  
Su objetivo es fortalecer el razonamiento lógico y matemático en jóvenes estudiantes mediante la resolución de Sudoku de diferentes tamaños y niveles de dificultad.

La aplicación está alineada con el proyecto de la Fundación para el Desarrollo Cognitivo (FDC), ofreciendo una experiencia educativa interactiva y orientada a áreas STEM.

---

## Características Principales
- Generación de tableros de Sudoku 4x4 y 9x9 de distintos niveles de dificultad (Fácil, Medio y Difícil) utilizando la API de API Ninjas.
- Resolución interactiva del Sudoku mediante una interfaz intuitiva desarrollada en Jetpack Compose.
- Funcionalidad de verificación de solución para comprobar si el tablero está correctamente resuelto.
- Reinicio de partida o solicitud de nuevo puzzle en cualquier momento.
- Guardado automático de partidas en progreso y opción para continuarlas después.
- Manejo de estados de la interfaz: Cargando, Error y Éxito.
- Funcionamiento offline.
- Diseño responsivo, adaptándose a distintos tamaños de pantalla.
- Aplicación de MVVM + Clean Architecture.
- Inyección de dependencias utilizando Hilt.

---

## Arquitectura del Proyecto
El proyecto implementa una estructura limpia siguiendo los principios de Clean Architecture:

```
app/
├── data/
│   ├── api/         # Lógica de conexión con la API externa
│   ├── cache/       # Base de datos local (Room)
│   └── repository/  # Implementación de los Repositorios
├── domain/
│   ├── model/       # Modelos del dominio
│   ├── repository/  # Interfaces de los Repositorios
│   └── usecase/     # Casos de uso de negocio
├── presentation/
│   ├── components/  # Componentes UI reutilizables (Compose)
│   ├── screens/     # Pantallas principales (Home, Game, Saved Games)
│   └── util/         # Clases auxiliares (manejadores de estado, etc.)
├── di/              # Módulos de inyección de dependencias (Hilt)
├── SudokuApp.kt     # Configuración principal de la app
└── build.gradle     # Configuración de dependencias
```

## Conexión a API
Se realiza la conexión al endpoint:
```
https://api.api-ninjas.com/v1/sudokugenerate?width=4&height=4&difficulty=easy
```
Utilizando el **API Key**:
```
wLVPN1zV08lJYF7uXqgyPw==zVwp6TlVcAO1NLUf
```
Incluyendo el encabezado `X-Api-Key` como parte de la solicitud HTTP.
```
Gracias por revisar mi proyecto.

