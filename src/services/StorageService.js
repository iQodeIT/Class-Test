import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Crypto from 'expo-crypto';

const STORAGE_KEYS = {
  PROPERTIES: '@moveproof_properties',
  ROOMS: '@moveproof_rooms',
  ITEMS: '@moveproof_items',
  LOGS: '@moveproof_logs',
  PRO_STATUS: '@moveproof_pro_status',
  ONBOARDING: '@moveproof_onboarding_completed',
};

// Initial Preset Checklist Items
export const DEFAULT_ROOM_CHECKLISTS = {
  'Kitchen': ['Walls', 'Floor', 'Ceiling', 'Stove & Oven', 'Refrigerator', 'Sink & Faucets', 'Cabinets', 'Countertops', 'Windows & Blinds', 'Outlets'],
  'Living Room': ['Walls & Baseboards', 'Flooring/Carpet', 'Ceiling & Light Fixtures', 'Windows & Blinds', 'Outlets & Switches', 'Doors & Hardware'],
  'Bedroom': ['Walls', 'Carpet/Flooring', 'Ceiling', 'Closet & Shelving', 'Windows', 'Outlets', 'Door & Lock'],
  'Bathroom': ['Walls & Tiles', 'Flooring', 'Sink & Vanity', 'Toilet', 'Shower/Tub', 'Mirror & Cabinet', 'Exhaust Fan', 'Towel Bars'],
  'Hallway': ['Walls', 'Flooring', 'Lighting', 'Smoke Detector'],
  'Balcony': ['Railing', 'Flooring/Deck', 'Patio Door', 'Outdoor Light'],
  'Garage': ['Floor/Concrete', 'Walls', 'Garage Door & Opener', 'Outlets'],
  'Closet': ['Shelves', 'Hanging Rods', 'Door', 'Walls & Floor']
};

export const StorageService = {
  // Onboarding
  async isOnboardingCompleted() {
    try {
      const val = await AsyncStorage.getItem(STORAGE_KEYS.ONBOARDING);
      return val === 'true';
    } catch (e) {
      return false;
    }
  },

  async setOnboardingCompleted(completed = true) {
    try {
      await AsyncStorage.setItem(STORAGE_KEYS.ONBOARDING, completed ? 'true' : 'false');
    } catch (e) {
      console.error('Error saving onboarding state:', e);
    }
  },

  // Properties
  async getProperties() {
    try {
      const data = await AsyncStorage.getItem(STORAGE_KEYS.PROPERTIES);
      return data ? JSON.parse(data) : [];
    } catch (e) {
      console.error('Error loading properties:', e);
      return [];
    }
  },

  async saveProperty(property) {
    try {
      const properties = await this.getProperties();
      const existingIdx = properties.findIndex(p => p.id === property.id);
      if (existingIdx >= 0) {
        properties[existingIdx] = { ...properties[existingIdx], ...property, updatedAt: new Date().toISOString() };
      } else {
        properties.push({
          id: property.id || Crypto.randomUUID(),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
          lastUnfinishedRoomId: null,
          ...property
        });
      }
      await AsyncStorage.setItem(STORAGE_KEYS.PROPERTIES, JSON.stringify(properties));
      return properties;
    } catch (e) {
      console.error('Error saving property:', e);
      throw e;
    }
  },

  async deleteProperty(propertyId) {
    try {
      let properties = await this.getProperties();
      properties = properties.filter(p => p.id !== propertyId);
      await AsyncStorage.setItem(STORAGE_KEYS.PROPERTIES, JSON.stringify(properties));

      // Clean up rooms & items
      let rooms = await this.getRooms();
      const roomIdsToRemove = rooms.filter(r => r.propertyId === propertyId).map(r => r.id);
      rooms = rooms.filter(r => r.propertyId !== propertyId);
      await AsyncStorage.setItem(STORAGE_KEYS.ROOMS, JSON.stringify(rooms));

      let items = await this.getItems();
      items = items.filter(i => !roomIdsToRemove.includes(i.roomId));
      await AsyncStorage.setItem(STORAGE_KEYS.ITEMS, JSON.stringify(items));
    } catch (e) {
      console.error('Error deleting property:', e);
    }
  },

  // Rooms
  async getRooms(propertyId = null) {
    try {
      const data = await AsyncStorage.getItem(STORAGE_KEYS.ROOMS);
      const rooms = data ? JSON.parse(data) : [];
      if (propertyId) {
        return rooms.filter(r => r.propertyId === propertyId);
      }
      return rooms;
    } catch (e) {
      return [];
    }
  },

  async createDefaultRoomsForProperty(propertyId, selectedRoomTypes = []) {
    try {
      const rooms = await this.getRooms();
      const newRooms = selectedRoomTypes.map((roomName, index) => ({
        id: Crypto.randomUUID(),
        propertyId,
        name: roomName,
        type: roomName,
        order: index,
        checklist: DEFAULT_ROOM_CHECKLISTS[roomName] || ['Walls', 'Floor', 'Ceiling', 'Fixtures', 'Windows', 'Outlets']
      }));
      const updatedRooms = [...rooms, ...newRooms];
      await AsyncStorage.setItem(STORAGE_KEYS.ROOMS, JSON.stringify(updatedRooms));
      return newRooms;
    } catch (e) {
      console.error('Error creating default rooms:', e);
      return [];
    }
  },

  async addCustomRoom(propertyId, roomName) {
    try {
      const rooms = await this.getRooms();
      const newRoom = {
        id: Crypto.randomUUID(),
        propertyId,
        name: roomName,
        type: 'Custom',
        order: rooms.length,
        checklist: ['Walls', 'Floor', 'Ceiling', 'Fixtures', 'Windows', 'Outlets']
      };
      await AsyncStorage.setItem(STORAGE_KEYS.ROOMS, JSON.stringify([...rooms, newRoom]));
      return newRoom;
    } catch (e) {
      console.error('Error adding custom room:', e);
      throw e;
    }
  },

  // Capture Items
  async getItems(propertyId = null, phase = null) {
    try {
      const data = await AsyncStorage.getItem(STORAGE_KEYS.ITEMS);
      let items = data ? JSON.parse(data) : [];
      if (propertyId) {
        items = items.filter(i => i.propertyId === propertyId);
      }
      if (phase) {
        items = items.filter(i => i.phase === phase);
      }
      return items;
    } catch (e) {
      return [];
    }
  },

  async saveItem(itemData) {
    try {
      const items = await this.getItems();
      const id = itemData.id || Crypto.randomUUID();
      const timestamp = new Date().toISOString();

      const newItem = {
        id,
        timestamp,
        condition: itemData.condition || 'Good', // 'Good', 'Minor wear', 'Damage', 'Pre-existing damage'
        note: itemData.note || '',
        mediaUri: itemData.mediaUri || null,
        mediaType: itemData.mediaType || 'photo', // 'photo' or 'video'
        annotatedUri: itemData.annotatedUri || null,
        sha256: itemData.sha256 || null,
        latitude: itemData.latitude || null,
        longitude: itemData.longitude || null,
        isPreExisting: itemData.condition === 'Pre-existing damage' || itemData.isPreExisting || false,
        closeUpUri: itemData.closeUpUri || null,
        wideShotUri: itemData.wideShotUri || null,
        ...itemData,
      };

      const existingIdx = items.findIndex(i => i.id === id);
      if (existingIdx >= 0) {
        items[existingIdx] = { ...items[existingIdx], ...newItem };
      } else {
        items.push(newItem);
      }

      await AsyncStorage.setItem(STORAGE_KEYS.ITEMS, JSON.stringify(items));
      return newItem;
    } catch (e) {
      console.error('Error saving capture item:', e);
      throw e;
    }
  },

  // Deposit Logs
  async getDepositLog(propertyId) {
    try {
      const data = await AsyncStorage.getItem(`${STORAGE_KEYS.LOGS}_${propertyId}`);
      return data ? JSON.parse(data) : {
        propertyId,
        forwardingAddressSentDate: null,
        depositReceivedDate: null,
        amountReturned: null,
        itemizedListReceived: false,
      };
    } catch (e) {
      return null;
    }
  },

  async saveDepositLog(propertyId, logData) {
    try {
      const current = await this.getDepositLog(propertyId);
      const updated = { ...current, ...logData, propertyId };
      await AsyncStorage.setItem(`${STORAGE_KEYS.LOGS}_${propertyId}`, JSON.stringify(updated));
      return updated;
    } catch (e) {
      console.error('Error saving deposit log:', e);
    }
  },

  // Pro Monetization Status
  async getProStatus() {
    try {
      const data = await AsyncStorage.getItem(STORAGE_KEYS.PRO_STATUS);
      return data ? JSON.parse(data) : { isPro: false, planType: 'free' };
    } catch (e) {
      return { isPro: false, planType: 'free' };
    }
  },

  async setProStatus(isPro, planType = 'pro_annual') {
    try {
      const status = { isPro, planType, updatedAt: new Date().toISOString() };
      await AsyncStorage.setItem(STORAGE_KEYS.PRO_STATUS, JSON.stringify(status));
      return status;
    } catch (e) {
      console.error('Error setting Pro status:', e);
    }
  },

  // Data Export / Reset
  async exportAllData() {
    try {
      const properties = await this.getProperties();
      const rooms = await this.getRooms();
      const items = await this.getItems();
      const proStatus = await this.getProStatus();
      return {
        exportedAt: new Date().toISOString(),
        properties,
        rooms,
        items,
        proStatus
      };
    } catch (e) {
      console.error('Error exporting data:', e);
      throw e;
    }
  },

  async clearAllData() {
    try {
      await AsyncStorage.multiRemove([
        STORAGE_KEYS.PROPERTIES,
        STORAGE_KEYS.ROOMS,
        STORAGE_KEYS.ITEMS,
        STORAGE_KEYS.PRO_STATUS,
        STORAGE_KEYS.ONBOARDING
      ]);
    } catch (e) {
      console.error('Error clearing data:', e);
    }
  }
};
