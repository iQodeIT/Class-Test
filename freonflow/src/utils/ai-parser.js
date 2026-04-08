/**
 * Utility for parsing unstructured job descriptions using Gemini AI.
 * Implement the system prompt and structured JSON response logic.
 */

const SYSTEM_PROMPT = `
You are an expert HVAC service coordinator. Your task is to parse messy, unstructured job notes from a technician and convert them into a structured JSON array of line items.

Each item in the array must have:
- description: A clear, professional name for the service or part.
- quantity: A numerical value (default to 1 if not specified).
- unit_price: An estimated market price if not specified, or the specific price if mentioned.

Example Input: "Swapped 2 AC capacitors, took 1.5 hours labor, freon recharge 3 lbs"
Example Output:
[
  {"description": "Dual Run Capacitor", "quantity": 2, "unit_price": 45.00},
  {"description": "Labor - Hourly Rate", "quantity": 1.5, "unit_price": 120.00},
  {"description": "R-410A Refrigerant", "quantity": 3, "unit_price": 95.00}
]

Return ONLY the JSON array. Do not include any conversational text.
`;

export const parseJobDescription = async (text) => {
  console.log("Parsing job description with AI:", text);

  /**
   * Placeholder for Gemini API call:
   *
   * const result = await model.generateContent([SYSTEM_PROMPT, text]);
   * return JSON.parse(result.response.text());
   */

  // Simulated delay and mock response for now, but with the requested structure
  await new Promise(resolve => setTimeout(resolve, 800));

  return [
    { description: "AC Capacitor replacement", quantity: 2, unit_price: 45.00 },
    { description: "Labor - Hourly", quantity: 1.5, unit_price: 125.00 },
    { description: "Refrigerant Recharge (lbs)", quantity: 3, unit_price: 90.00 }
  ];
};
