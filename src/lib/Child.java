package lib;

/**
 * Represents child data.
 * Using a record for conciseness and immutability.
 *
 * @param name Child's full name.
 * @param idNumber Child's ID number.
 */
public record Child(String name, String idNumber) {}