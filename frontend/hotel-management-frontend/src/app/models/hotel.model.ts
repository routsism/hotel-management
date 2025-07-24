export interface HotelInsertDTO {
  name: string;
  address: string;
  phone?: string;  
  email?: string;
}

export interface HotelReadOnlyDTO {
  id: number;
  name: string;
  address: string;
  phone?: string;
  email?: string;
}