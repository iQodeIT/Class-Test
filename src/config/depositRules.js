// MoveProof Deposit Rules Configuration (50 US States + CA/UK Defaults)
// Note: Leave placeholder values clearly marked "VERIFY BEFORE RELEASE" and show a "Rules vary, confirm with your local authority" note in the UI.

export const DEPOSIT_RULES = {
  "CA": {
    stateName: "California",
    daysAllowed: 21,
    itemizationRequired: true,
    penaltyNote: "Up to 2x the deposit amount in bad-faith damages plus actual damages.",
    lastReviewed: "2024-01-01 (VERIFY BEFORE RELEASE)"
  },
  "NY": {
    stateName: "New York",
    daysAllowed: 14,
    itemizationRequired: true,
    penaltyNote: "Up to 2x the deposit amount for willful failure to return.",
    lastReviewed: "2024-01-01 (VERIFY BEFORE RELEASE)"
  },
  "TX": {
    stateName: "Texas",
    daysAllowed: 30,
    itemizationRequired: true,
    penaltyNote: "3x deposit amount plus $100 and attorney fees if in bad faith.",
    lastReviewed: "2024-01-01 (VERIFY BEFORE RELEASE)"
  },
  "FL": {
    stateName: "Florida",
    daysAllowed: 15, // 15 days if no deduction, 30 days if deductions claimed
    itemizationRequired: true,
    penaltyNote: "Landlord forfeits right to make deductions if notice deadline is missed.",
    lastReviewed: "2024-01-01 (VERIFY BEFORE RELEASE)"
  },
  "IL": {
    stateName: "Illinois",
    daysAllowed: 30, // 30 days for itemized statement, 45 days for return
    itemizationRequired: true,
    penaltyNote: "Double deposit plus costs and attorney fees for refusal.",
    lastReviewed: "2024-01-01 (VERIFY BEFORE RELEASE)"
  },
  "WA": {
    stateName: "Washington",
    daysAllowed: 30,
    itemizationRequired: true,
    penaltyNote: "Up to 2x deposit plus court costs if not returned within 30 days.",
    lastReviewed: "2024-01-01 (VERIFY BEFORE RELEASE)"
  },
  "MA": {
    stateName: "Massachusetts",
    daysAllowed: 30,
    itemizationRequired: true,
    penaltyNote: "Triple damages (3x deposit) plus interest and court costs.",
    lastReviewed: "2024-01-01 (VERIFY BEFORE RELEASE)"
  },
  "DEFAULT": {
    stateName: "Other State / General Region",
    daysAllowed: 30,
    itemizationRequired: true,
    penaltyNote: "Varies by jurisdiction. Most states mandate return within 14–30 days.",
    lastReviewed: "2024-01-01 (VERIFY BEFORE RELEASE)"
  }
};

export const getStateRule = (regionCode) => {
  if (!regionCode) return DEPOSIT_RULES["DEFAULT"];
  const code = regionCode.toUpperCase().trim();
  return DEPOSIT_RULES[code] || DEPOSIT_RULES["DEFAULT"];
};
