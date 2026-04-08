import { GoogleGenerativeAI } from "@google/generative-ai";

/**
 * Utility for parsing unstructured job descriptions using Gemini API.
 */

const API_KEY = import.meta.env.VITE_GEMINI_API_KEY;
const genAI = API_KEY ? new GoogleGenerativeAI(API_KEY) : null;

const SYSTEM_PROMPT = `You are an HVAC office assistant. Extract parts, quantities, and estimated prices from this messy technician note. Return ONLY a JSON array of objects with the keys: description, quantity, and unit_price.`;

export const parseJobDescription = async (text) => {
  console.log("Parsing job description with Gemini API:", text);

  if (!genAI) {
    console.warn("Gemini API key not found. Falling back to mock data.");
    await new Promise(resolve => setTimeout(resolve, 1000));
    return [
      { description: "Replacement Dual Run Capacitor", quantity: 1, unit_price: 65.00 },
      { description: "R-410A Refrigerant Charge (lbs)", quantity: 2, unit_price: 110.00 }
    ];
  }

  try {
    const model = genAI.getGenerativeModel({ model: "gemini-pro" });
    const prompt = `${SYSTEM_PROMPT}\n\nTechnician Note: "${text}"`;

    const result = await model.generateContent(prompt);
    const response = await result.response;
    const jsonString = response.text().replace(/```json|```/g, "").trim();

    return JSON.parse(jsonString);
  } catch (error) {
    console.error("Gemini API Error:", error);
    throw new Error("Failed to parse job description with AI.");
  }
};
