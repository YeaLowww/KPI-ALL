import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from scipy.interpolate import splprep, splev


def create_pyramid(base_points, apex, num_points):
    pyramid = []
    for i in range(len(base_points)):
        points = [base_points[i], base_points[(i+1)%len(base_points)], apex]
        for j in range(len(points)):
            face = np.array([points[j], points[(j+1)%len(points)], apex])
            pyramid.append(face)
    return pyramid

def z_buffer_algorithm(faces, view_point, num_points):
    visible_faces = []
    z_buffer = np.zeros((num_points, num_points))
    for face in faces:
        normal = np.cross(face[1]-face[0], face[-1]-face[0])
        if np.dot(normal, face[0]-view_point) < 0:
            visible_faces.append(face)
            for i in range(num_points):
                for j in range(num_points):
                    z = np.dot(normal, face[0] - view_point) / np.dot(normal, np.array([i, j, 0]) - view_point)
                    if z > z_buffer[i, j]:
                        z_buffer[i, j] = z
    return visible_faces
def plot_spline(ax, points, num_points, color='r'):
    tck, u = splprep(points.T, k=2, s=0)
    u_new = np.linspace(u.min(), u.max(), num_points)
    spline_points = splev(u_new, tck)
    ax.plot3D(*spline_points, color=color)
# Parameters of the pyramid
base_points = [(0, 0, 0), (1, 0, 0), (1, 1, 0), (0, 1, 0)]
apex = (0.5, 0.5, 1)
num_points = 20
view_point = np.array([-0.5, -0.5, 1])

# Create the pyramid
pyramid = create_pyramid(base_points, apex, num_points)

# Apply z-buffer algorithm to remove hidden faces
visible_pyramid = z_buffer_algorithm(pyramid, view_point, num_points)

# Plotting the pyramid

fig = plt.figure(figsize=(6, 6))
ax = fig.add_subplot(111, projection='3d')


# Plot the visible sides using spline interpolation
for face in visible_pyramid:
    points = np.array([face[i] for i in range(len(face))])
    plot_spline(ax, points, num_points, color='r')
# Plot the unvisible sides using spline interpolation

# Plot the base
base_edges = [[base_points[i], base_points[(i + 1) % len(base_points)]] for i in range(len(base_points))]
for edge in base_edges:
    ax.plot3D(*zip(*edge), color="w")

# Plot the sides
for point in base_points:
    ax.plot3D(*zip(point, apex), color="w")

# Plot the apex
ax.scatter(*zip(apex), color='g')

# Plot the visible faces
for face in visible_pyramid:
    vertices = np.array(face)
    for i in range(len(vertices)):
        ax.plot([vertices[i][0], vertices[(i+1)%len(vertices)][0]],
                [vertices[i][1], vertices[(i+1)%len(vertices)][1]],
                [vertices[i][2], vertices[(i+1)%len(vertices)][2]], color='blue', alpha=0.3)



ax.set_xlim(-0.5, 1.5)
ax.set_ylim(-0.5, 1.5)
ax.set_zlim(-0.5, 1.5)
ax.set_box_aspect((1, 1, 1))

plt.tight_layout()
plt.show()
