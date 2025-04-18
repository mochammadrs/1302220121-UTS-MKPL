package lib;

/**
 * Represents spouse data.
 * Using a record for conciseness and immutability.
 *
 * @param name Spouse's full name.
 * @param idNumber Spouse's ID number.
 */
public record Spouse(String name, String idNumber) {}