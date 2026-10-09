import React, { useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  SafeAreaView,
  Alert
} from 'react-native';
import { X, Building2, MapPin, Calendar, DollarSign, User, Plus, Check } from 'lucide-react-native';
import { useApp } from '../context/AppContext';
import { StorageService } from '../services/StorageService';
import { DEPOSIT_RULES } from '../config/depositRules';

const PRESET_ROOMS = [
  'Living Room',
  'Kitchen',
  'Bedroom',
  'Bathroom',
  'Hallway',
  'Balcony',
  'Garage',
  'Closet'
];

export default function AddPropertyModal({ goBack, navigateTo }) {
  const { colors, refreshProperties } = useApp();

  // Form Fields
  const [nickname, setNickname] = useState('');
  const [address, setAddress] = useState('');
  const [regionCode, setRegionCode] = useState('CA');
  const [landlordName, setLandlordName] = useState('');
  const [landlordEmail, setLandlordEmail] = useState('');
  const [leaseStart, setLeaseStart] = useState('');
  const [leaseEnd, setLeaseEnd] = useState('');
  const [moveOutDate, setMoveOutDate] = useState('');
  const [depositAmount, setDepositAmount] = useState('');

  // Room Counts & Custom Rooms
  const [selectedRooms, setSelectedRooms] = useState([
    'Living Room',
    'Kitchen',
    'Bedroom',
    'Bathroom'
  ]);
  const [customRoomName, setCustomRoomName] = useState('');

  const toggleRoomSelect = (room) => {
    if (selectedRooms.includes(room)) {
      setSelectedRooms(selectedRooms.filter(r => r !== room));
    } else {
      setSelectedRooms([...selectedRooms, room]);
    }
  };

  const handleAddCustomRoom = () => {
    if (!customRoomName.trim()) return;
    const name = customRoomName.trim();
    if (!selectedRooms.includes(name)) {
      setSelectedRooms([...selectedRooms, name]);
    }
    setCustomRoomName('');
  };

  const handleSave = async () => {
    if (!address.trim()) {
      Alert.alert('Required Field', 'Please enter the property address.');
      return;
    }

    if (selectedRooms.length === 0) {
      Alert.alert('Select Rooms', 'Please select at least one room to document.');
      return;
    }

    try {
      const newProperty = {
        nickname: nickname.trim() || address.split(',')[0],
        address: address.trim(),
        region: regionCode,
        landlordName: landlordName.trim(),
        landlordEmail: landlordEmail.trim(),
        leaseStart: leaseStart.trim(),
        leaseEnd: leaseEnd.trim(),
        moveOutDate: moveOutDate.trim(),
        depositAmount: depositAmount ? parseFloat(depositAmount) : null,
      };

      const savedProps = await StorageService.saveProperty(newProperty);
      const createdProp = savedProps[savedProps.length - 1];

      // Create initial rooms for this property
      await StorageService.createDefaultRoomsForProperty(createdProp.id, selectedRooms);

      await refreshProperties();
      navigateTo('PROPERTY_DETAIL', { propertyId: createdProp.id });
    } catch (e) {
      console.error('Error adding property:', e);
      Alert.alert('Error', 'Failed to create property.');
    }
  };

  return (
    <SafeAreaView style={[styles.container, { backgroundColor: colors.background }]}>
      {/* Header */}
      <View style={[styles.header, { borderBottomColor: colors.border }]}>
        <TouchableOpacity onPress={goBack} style={styles.closeBtn}>
          <X size={24} color={colors.textPrimary} />
        </TouchableOpacity>
        <Text style={[styles.headerTitle, { color: colors.textPrimary }]}>Add Rental Property</Text>
        <TouchableOpacity onPress={handleSave} style={styles.saveHeaderBtn}>
          <Text style={[styles.saveHeaderText, { color: colors.primary }]}>Save</Text>
        </TouchableOpacity>
      </View>

      <ScrollView contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {/* Basic Info Section */}
        <View style={styles.section}>
          <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Property Details</Text>

          {/* Address */}
          <Text style={[styles.label, { color: colors.textSecondary }]}>Address *</Text>
          <View style={[styles.inputContainer, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <MapPin size={20} color={colors.textMuted} style={styles.inputIcon} />
            <TextInput
              style={[styles.input, { color: colors.textPrimary }]}
              placeholder="e.g. 123 Main St, Apt 4B, San Francisco, CA"
              placeholderTextColor={colors.textMuted}
              value={address}
              onChangeText={setAddress}
            />
          </View>

          {/* Nickname */}
          <Text style={[styles.label, { color: colors.textSecondary }]}>Nickname (Optional)</Text>
          <View style={[styles.inputContainer, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <Building2 size={20} color={colors.textMuted} style={styles.inputIcon} />
            <TextInput
              style={[styles.input, { color: colors.textPrimary }]}
              placeholder="e.g. Sunset Apartments #4B"
              placeholderTextColor={colors.textMuted}
              value={nickname}
              onChangeText={setNickname}
            />
          </View>

          {/* State / Region Rule Selector */}
          <Text style={[styles.label, { color: colors.textSecondary }]}>State / Jurisdiction (Deposit Rules)</Text>
          <View style={styles.regionChipContainer}>
            {Object.keys(DEPOSIT_RULES).map(code => (
              <TouchableOpacity
                key={code}
                style={[
                  styles.regionChip,
                  {
                    backgroundColor: regionCode === code ? colors.primary : colors.surface,
                    borderColor: regionCode === code ? colors.primary : colors.border
                  }
                ]}
                onPress={() => setRegionCode(code)}
              >
                <Text
                  style={[
                    styles.regionChipText,
                    { color: regionCode === code ? '#FFFFFF' : colors.textPrimary }
                  ]}
                >
                  {code}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>

        {/* Deposit & Lease Section */}
        <View style={styles.section}>
          <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Deposit & Lease Info</Text>

          {/* Deposit Amount */}
          <Text style={[styles.label, { color: colors.textSecondary }]}>Security Deposit Amount ($)</Text>
          <View style={[styles.inputContainer, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <DollarSign size={20} color={colors.textMuted} style={styles.inputIcon} />
            <TextInput
              style={[styles.input, { color: colors.textPrimary }]}
              placeholder="e.g. 2500"
              placeholderTextColor={colors.textMuted}
              keyboardType="numeric"
              value={depositAmount}
              onChangeText={setDepositAmount}
            />
          </View>

          {/* Move Out Date */}
          <Text style={[styles.label, { color: colors.textSecondary }]}>Move-Out Date (YYYY-MM-DD)</Text>
          <View style={[styles.inputContainer, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <Calendar size={20} color={colors.textMuted} style={styles.inputIcon} />
            <TextInput
              style={[styles.input, { color: colors.textPrimary }]}
              placeholder="e.g. 2025-06-30"
              placeholderTextColor={colors.textMuted}
              value={moveOutDate}
              onChangeText={setMoveOutDate}
            />
          </View>
        </View>

        {/* Landlord Info Section */}
        <View style={styles.section}>
          <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Landlord / Property Manager</Text>

          <Text style={[styles.label, { color: colors.textSecondary }]}>Landlord Name (Optional)</Text>
          <View style={[styles.inputContainer, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <User size={20} color={colors.textMuted} style={styles.inputIcon} />
            <TextInput
              style={[styles.input, { color: colors.textPrimary }]}
              placeholder="e.g. Jane Doe"
              placeholderTextColor={colors.textMuted}
              value={landlordName}
              onChangeText={setLandlordName}
            />
          </View>

          <Text style={[styles.label, { color: colors.textSecondary }]}>Landlord Email (Optional)</Text>
          <View style={[styles.inputContainer, { backgroundColor: colors.surface, borderColor: colors.border }]}>
            <User size={20} color={colors.textMuted} style={styles.inputIcon} />
            <TextInput
              style={[styles.input, { color: colors.textPrimary }]}
              placeholder="landlord@example.com"
              placeholderTextColor={colors.textMuted}
              keyboardType="email-address"
              value={landlordEmail}
              onChangeText={setLandlordEmail}
            />
          </View>
        </View>

        {/* Room Selector Chips */}
        <View style={styles.section}>
          <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Select Rooms to Document</Text>
          <Text style={[styles.subNote, { color: colors.textSecondary }]}>
            Tap to add or remove rooms. You can also add custom spaces.
          </Text>

          <View style={styles.roomsChipContainer}>
            {PRESET_ROOMS.map((room) => {
              const isSelected = selectedRooms.includes(room);
              return (
                <TouchableOpacity
                  key={room}
                  style={[
                    styles.roomChip,
                    {
                      backgroundColor: isSelected ? colors.primaryLight : colors.surface,
                      borderColor: isSelected ? colors.primary : colors.border
                    }
                  ]}
                  onPress={() => toggleRoomSelect(room)}
                >
                  {isSelected && <Check size={16} color={colors.primary} style={{ marginRight: 6 }} />}
                  <Text
                    style={[
                      styles.roomChipText,
                      { color: isSelected ? colors.primary : colors.textPrimary }
                    ]}
                  >
                    {room}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </View>

          {/* Add Custom Room Input */}
          <View style={styles.customRoomRow}>
            <TextInput
              style={[
                styles.customRoomInput,
                { backgroundColor: colors.surface, borderColor: colors.border, color: colors.textPrimary }
              ]}
              placeholder="Add Custom Room (e.g. Storage Unit)"
              placeholderTextColor={colors.textMuted}
              value={customRoomName}
              onChangeText={setCustomRoomName}
            />
            <TouchableOpacity
              style={[styles.addCustomBtn, { backgroundColor: colors.primary }]}
              onPress={handleAddCustomRoom}
            >
              <Plus size={20} color="#FFFFFF" />
            </TouchableOpacity>
          </View>
        </View>

        {/* Save CTA */}
        <TouchableOpacity
          style={[styles.saveBtn, { backgroundColor: colors.primary }]}
          onPress={handleSave}
          activeOpacity={0.85}
        >
          <Text style={styles.saveBtnText}>Create Property & Start Capture</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    height: 56,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    borderBottomWidth: 1,
  },
  closeBtn: {
    padding: 4,
  },
  headerTitle: {
    fontSize: 18,
    fontWeight: '700',
  },
  saveHeaderBtn: {
    paddingVertical: 4,
    paddingHorizontal: 8,
  },
  saveHeaderText: {
    fontSize: 16,
    fontWeight: '700',
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingVertical: 20,
  },
  section: {
    marginBottom: 24,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '800',
    marginBottom: 12,
  },
  subNote: {
    fontSize: 13,
    marginBottom: 12,
  },
  label: {
    fontSize: 13,
    fontWeight: '600',
    marginBottom: 6,
    marginTop: 8,
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    height: 52,
    borderRadius: 14,
    borderWidth: 1,
    paddingHorizontal: 14,
  },
  inputIcon: {
    marginRight: 10,
  },
  input: {
    flex: 1,
    fontSize: 15,
  },
  regionChipContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginTop: 6,
  },
  regionChip: {
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 12,
    borderWidth: 1,
  },
  regionChipText: {
    fontSize: 13,
    fontWeight: '700',
  },
  roomsChipContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginVertical: 10,
  },
  roomChip: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 14,
    paddingVertical: 10,
    borderRadius: 14,
    borderWidth: 1,
  },
  roomChipText: {
    fontSize: 14,
    fontWeight: '600',
  },
  customRoomRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    marginTop: 8,
  },
  customRoomInput: {
    flex: 1,
    height: 48,
    borderRadius: 12,
    borderWidth: 1,
    paddingHorizontal: 14,
    fontSize: 14,
  },
  addCustomBtn: {
    width: 48,
    height: 48,
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
  },
  saveBtn: {
    height: 56,
    borderRadius: 16,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 12,
    marginBottom: 40,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 8,
    elevation: 4,
  },
  saveBtnText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '700',
  }
});
