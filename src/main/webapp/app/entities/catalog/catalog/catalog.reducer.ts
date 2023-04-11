import axios from 'axios';
import {createAsyncThunk, isFulfilled, isPending, isRejected, PayloadAction} from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ICatalog, defaultValue } from 'app/shared/model/catalog/catalog.model';

const initialState: EntityState<ICatalog> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  catalog: {
    groups:[],
    types:[]
  }
};

const apiUrl = 'services/catalog/api/catalogs';

// Actions

// export const getEntities = createAsyncThunk('catalog/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
//   const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
//   return axios.get<ICatalog[]>(requestUrl);
// });


export const getEntities = createAsyncThunk('tag/fetch_entity_group', async ({groupUuid, type}:{groupUuid: string, type:string}) => {
  const requestUrl = `${apiUrl}/${groupUuid}/${type}`;
  return axios.get<ICatalog[]>(requestUrl);
});


export const getEntity = createAsyncThunk(
  'catalog/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ICatalog>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getGroupList = createAsyncThunk(
  'catalog/fetch_group_list',
  async () => {
    const requestUrl = `${apiUrl}/groups`;
    return axios.get<string[]>(requestUrl);
  }
)

export const getGroupTypeList = createAsyncThunk(
  'catalog/fetch_group_type_list',
  async (groupUuid:string) => {
    const requestUrl = `${apiUrl}/groups/${groupUuid}`;
    return axios.get<string[]>(requestUrl);

  }
)


export const createEntity = createAsyncThunk(
  'catalog/create_entity',
  async (entity: ICatalog, thunkAPI) => {
    const result = await axios.post<ICatalog>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({groupUuid: result.data.groupUuid, type: result.data.type}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'catalog/update_entity',
  async (entity: ICatalog, thunkAPI) => {
    const result = await axios.put<ICatalog>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({groupUuid: result.data.groupUuid, type: result.data.type}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'catalog/partial_update_entity',
  async (entity: ICatalog, thunkAPI) => {
    const result = await axios.patch<ICatalog>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({groupUuid: result.data.groupUuid, type: result.data.type}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'catalog/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ICatalog>(requestUrl);
    thunkAPI.dispatch(getEntities({groupUuid: result.data.groupUuid, type: result.data.type}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const CatalogSlice = createEntitySlice({
  name: 'catalog',
  initialState,
  reducers: {
    getGroupTypeCatalog: (state, action:PayloadAction<{groupUuid:string, type:string}>) => {
      state.catalog[action.payload.groupUuid + action.payload.type] = {}
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addCase(getGroupList.fulfilled, (state, action) => {
        state.catalog['groups'] = action.payload.data.map(d=>{
          const  o = {value:d}
          return o
        })
      })
      .addCase(getGroupTypeList.fulfilled, (state, action) => {
        state.catalog['types'] = action.payload.data.map(d=>{
          const  o = {value:d}
          return o
        })
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;
        console.log(headers)
        const groupUuid = headers['groupuuid']
        const type = headers['type']
        const key = groupUuid+type
        data.map(catalog=>{
          state.catalog[key][catalog.name] =  catalog
        })
        state.loading = false
        state.entities = data

        state= {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),

        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset, getGroupTypeCatalog } = CatalogSlice.actions;

// Reducer
export default CatalogSlice.reducer;
