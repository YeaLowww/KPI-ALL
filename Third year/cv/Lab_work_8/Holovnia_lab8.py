import numpy as np
import cv2
import open3d as o3d
def write_point_cloud(filename, vertices, colors):
    vertices = vertices.reshape(-1, 3)
    colors = colors.reshape(-1, 3)
    vertices_with_colors = np.hstack([vertices, colors])
    ply_header = f"ply\nformat ascii 1.0\nelement vertex {len(vertices_with_colors)}\n" \
                 "property float x\nproperty float y\nproperty float z\n" \
                 "property uchar red\nproperty uchar green\nproperty uchar blue\nend_header\n"
    with open(filename, 'w') as file:
        file.write(ply_header)
        np.savetxt(file, vertices_with_colors, fmt='%f %f %f %d %d %d')

def img_reconstruction(image_paths):
    img1, img2 = [cv2.imread(path) for path in image_paths[:2]]
    if img1 is None or img2 is None:
        raise ValueError("Images could not be loaded")

    window_size = 3
    min_disparity = 30
    num_disparities = 120 - min_disparity

    stereo = cv2.StereoSGBM_create(
        minDisparity=min_disparity,
        numDisparities=num_disparities,
        blockSize=16,
        P1=8 * 2 * window_size**2,
        P2=32 * 2 * window_size**2,
        disp12MaxDiff=1,
        uniquenessRatio=10,
        speckleWindowSize=100,
        speckleRange=32
    )

    disparity = stereo.compute(img1, img2).astype(np.float32) / 16.0

    height, width = img1.shape[:2]
    focal_length = 0.9 * width
    Q = np.float32([[1, 0, 0, -0.5 * width],
                    [0, -1, 0, 0.5 * height],
                    [0, 0, 0, -focal_length],
                    [0, 0, 1, 0]])
    points = cv2.reprojectImageTo3D(disparity, Q.astype(np.float32))
    colors = cv2.cvtColor(img1, cv2.COLOR_BGR2RGB)
    mask = disparity > disparity.min()
    out_points = points[mask]
    out_colors = colors[mask]

    output_file = 'point_cloud.ply'
    write_point_cloud(output_file, out_points, out_colors)
    
    cv2.namedWindow('map', cv2.WINDOW_NORMAL)
    cv2.imshow('map', (disparity - min_disparity) / num_disparities)
    cv2.resizeWindow('map', 650, 650)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

    # Load and render the point cloud

    pcd = o3d.io.read_point_cloud(output_file)
    o3d.visualization.draw_geometries([pcd], width=650, height=650, left=60, top=60)

if __name__ == '__main__':
    image_paths = ['photo_3.jpg', 'photo_2.jpg']
    try:
        img_reconstruction(image_paths)
    except Exception as e:
        print(f"An error occurred: {e}")
