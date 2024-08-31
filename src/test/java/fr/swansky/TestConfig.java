package fr.swansky;

public final class TestConfig {
    public String name;
    public int value;

    public TestConfig() {
    }

    public TestConfig(String name, int value) {
        this.name = name;
        this.value = value;
    }

    // equals and hashCode for comparison in tests
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestConfig that = (TestConfig) o;

        if (value != that.value) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + value;
        return result;
    }
}
