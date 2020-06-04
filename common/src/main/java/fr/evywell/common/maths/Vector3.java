package fr.evywell.common.maths;

public class Vector3 {

    public float x, y, z;
    private float magnitude;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vector3 add(Vector3 v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vector3 sub(Vector3 v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vector3 multiply(float value) {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }

    public Vector3 multiply(int value) {
        x *= value;
        y *= value;
        z *= value;
        return this;
    }

    public Vector3 multiply(Vector3 v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        return this;
    }

    public boolean moreThan(Vector3 v) {
        return x > v.x || y > v.y || z > v.z;
    }

    public boolean lessThan(Vector3 v) {
        return x < v.x || y < v.y || z < v.z;
    }

    public Vector3 normalize() {
        float magnitude = getMagnitude();
        return new Vector3(x / magnitude, y / magnitude, z / magnitude);
    }

    public float getMagnitude() {
        if (magnitude == 0.0f) {
            magnitude = (float) Math.sqrt((x*x) + (y*y) + (z*z));
        }
        return magnitude;
    }

}
