import time
import numpy as np
from graphics import GraphWin, Polygon, Point

WINDOW_WEIGHT = 600
WINDOW_HEIGHT = 600
CENTER = np.array((WINDOW_WEIGHT // 2, WINDOW_HEIGHT // 2))

ONES = np.array([
    [1],
    [1],
    [1],
])

TETA = 3


R = np.array([
    [np.cos(TETA), np.sin(TETA), 0],
    [-np.sin(TETA), np.cos(TETA), 0],
    [0, 0, 1],
])

T = np.array([
    [1.05, 0, 0],
    [0, 1.05, 0],
    [0, 0, 1],
])

m = np.array([5, 10])

M = np.array([
    [1, 0, 0],
    [0, 1, 0],
    [m[0], m[1], 1],
])

T_speed = 1.05

win = GraphWin("Rectangle", WINDOW_WEIGHT, WINDOW_HEIGHT)
win.setBackground('light blue')


def remove_rectangle(rectangle):
    rectangle.setFill("light blue")

def redraw_rectangle(points, rectangle):
    if rectangle:
        remove_rectangle(rectangle)
    rectangle = Polygon(*[Point(int(x), int(y)) for (x, y) in points])
    rectangle.setFill('orange')
    rectangle.draw(win)
    return rectangle


def rectangle_center(points):
    return np.array(((points[0][0] + points[1][0] + points[2][0] + points[3][0]) / 4,
                     (points[0][1] + points[1][1] + points[2][1] + points[3][1]) / 4))



def transform(points, A):
    center = rectangle_center(points)
    P = np.concatenate(
        (points - center, np.ones((len(points), 1))),
        axis=1
    )
    P_stroke = P @ A
    return P_stroke[:, :2] + center



def compositional_transformations(rectangle_points):
    rectangle = None
    for i in range(100):
        rectangle_points = transform(rectangle_points, (R @ T @ M))
        rectangle = redraw_rectangle(rectangle_points, rectangle)

        # -----------Change direction-----------
        if (i // 20) % 2 == 0:
            T[0, 0], T[1, 1] = T_speed, T_speed
            M[2, 0], M[2, 1] = m[0], m[1]
        else:
            T[0, 0], T[1, 1] = 1 / T_speed, 1 / T_speed
            M[2, 0], M[2, 1] = -m[0], -m[1]
        time.sleep(0.3)
    remove_rectangle(rectangle)
    return rectangle



def main():
    rectangle_points = np.array([
        [100, 100],
        [100, 200],
        [300, 200],
        [300, 100],
    ])

    rectangle = compositional_transformations(rectangle_points)

    win.getMouse()
    win.close()
    
    
    
if __name__ == '__main__':
    main()
