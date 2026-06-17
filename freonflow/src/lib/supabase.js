import { createClient } from '@supabase/supabase-js'

const supabaseUrl = import.meta.env.VITE_SUPABASE_URL || 'https://your-project.supabase.co'
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY || 'your-anon-key'

export const supabase = createClient(supabaseUrl, supabaseAnonKey)

/**
 * Database Schema Reference:
 *
 * users: (id, email, company_name, phone_number)
 * clients: (id, user_id, full_name, address, phone)
 * quotes: (id, user_id, client_id, status [draft, sent, approved], total_amount, created_at, notes)
 * quote_items: (id, quote_id, description, quantity, unit_price)
 */
