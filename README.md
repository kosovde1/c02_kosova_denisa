# PGRF1: Task 01: Line and Polygon rasterization

Starter project for the **PGRF1** course at the Faculty of Informatics and Management, University of Hradec Králové.

Author: Denisa Košová
Course: PGRF1 – Computer Graphics
Year: 2026

## Running the project

Clone or download the repository, open the project in your IDE, and run the `Main` class.
The project requires Java 21 or newer.

## Project structure

* `Main` – application entry point
* `Window` – creates and configures the main application window
* `Controller` – handles user input and coordinates application flow
* `Canvas` – provides the raster drawing surface and displays the rendered image

## Implementation
The application supports two drawing modes: independent line segments and polygon outlines.
* `Point, Line, Polygon`– represent geometric objects
* `Raster, RasterImage` – provide raster image operations
* `TrivialLineRasterizer` – rasterizes line using the trivial line rasterizing algorithm
* `PolygonOutlineRasterizer` – draws polygon outline using the line rasterizer

Both drawing modes support a preview line while dragging the mouse.
Switching between drawing modes preserves previously drawn objects.

## Controls
L - Switch to line drawing mode (default)

P - Switch to polygon drawing mode

Left mouse button:press and drag - Preview a line segment or polygon edge

Left mouse button: release - Finish a line segment or add a polygon vertex

Enter - Finish and close the polygon

C - Clear the canvas and start a new drawing
## Course

**PGRF1 (Computer Graphics)**  
Faculty of Informatics and Management  
University of Hradec Králové