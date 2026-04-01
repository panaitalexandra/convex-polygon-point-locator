# convex-polygon-point-locator
# Point Positioning in Convex Polygons (Wedge Method)

## 1. Requirement

The objective is to classify the position of a point **M(x_M, y_M)** relative to a **convex polygon** defined by vertices **A₁, A₂, ..., Aₙ**.

The algorithm determines if point **M** is:
* **Inside** the polygon
* **Outside** the polygon
* **On the Boundary** (on an edge or vertex)

## 2. Methodology: The Wedge Method (Binary Search)

Unlike general polygons, convex polygons allow for a high-performance approach ($O(\log n)$ complexity) by partitioning the polygon into "wedges" emanating from a central reference point.

### Step 1: Reference Center Identification
We calculate the **centroid G(g_x, g_y)** of the polygon as the arithmetic mean of all vertices:
$$g_x = \frac{1}{n} \sum_{i=1}^{n} x_i, \quad g_y = \frac{1}{n} \sum_{i=1}^{n} y_i$$
The vertices are then sorted angularly around this center **G**.

### Step 2: Binary Search
Using binary search on the sorted vertices, we identify the specific "wedge" (a triangle formed by $G, P_i, P_{i+1}$) that angularly encloses point **M** relative to center **G**.

### Step 3: Orientation Test (Determinant)
Once the correct segment $P_i P_{i+1}$ is found, we perform an orientation test using the determinant:
$$\text{det} = (x_{i+1} - x_i)(y_M - y_i) - (y_{i+1} - y_i)(x_M - x_i)$$

* **det > 0**: Point M is to the left of the segment → **Inside**
* **det < 0**: Point M is to the right of the segment → **Exterior**
* **det = 0**: Point M is collinear with the segment → **Boundary**

---

## 3. Practical Example

### Polygon Vertices (Convex)
The vertices form a symmetric octagonal shape:
`(0, 19), (14, 14), (19, 0), (14, -14), (0, -19), (-14, -14), (-19, 0), (-14, 14)`

### Calculation
1. **Centroid:** For this set, $\sum x_i = 0$ and $\sum y_i = 0$, resulting in **G(0, 0)**.
2. **Execution:** The algorithm locates the relevant angular sector for any test point **M** and uses the determinant relative to that sector's outer edge to provide the final classification.
