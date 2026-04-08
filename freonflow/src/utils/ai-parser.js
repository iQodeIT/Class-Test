/**
 * Utility for parsing unstructured job descriptions using Gemini AI.
 * Implement the system prompt and structured JSON response logic.
 */

const SYSTEM_PROMPT = `You are an HVAC office assistant. Extract parts, quantities, and estimated prices from this messy technician note. Return ONLY a JSON array of objects with the keys: description, quantity, and unit_price.`;

export const parseJobDescription = async (text) => {
  console.log("Parsing job description with AI assistant:", text);

  /**
   * Placeholder for Gemini API call:
   *
   * const result = await model.generateContent([SYSTEM_PROMPT, text]);
   * return JSON.parse(result.response.text());
   */

  // Simulated delay for UI spinner demonstration
  await new Promise(resolve => setTimeout(resolve, 1500));

  // Simulated structured response
  return [
    { description: "Replacement Dual Run Capacitor", quantity: 1, unit_price: 65.00 },
    { description: "R-410A Refrigerant Charge (lbs)", quantity: 2, unit_price: 110.00 },
    { description: "Hourly Service Labor", quantity: 1.5, unit_price: 125.00 }
  ];
};
