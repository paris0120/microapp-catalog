export interface ICatalog {
  id?: number;
  name?: string;
  hover?: string | null;
  description?: string | null;
  color?: string | null;
  link?: string | null;
  icon?: string | null;
  weight?: number | null;
  groupUuid?: string | null;
  isActive?: boolean | null;
  type?: string | null;
}

export const defaultValue: Readonly<ICatalog> = {
  isActive: false,
};
