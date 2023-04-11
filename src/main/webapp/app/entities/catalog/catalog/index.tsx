import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Catalog from './catalog';
import CatalogDetail from './catalog-detail';
import CatalogUpdate from './catalog-update';
import CatalogDeleteDialog from './catalog-delete-dialog';

const CatalogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Catalog />} />
    <Route path="new" element={<CatalogUpdate />} />
    <Route path=":id">
      <Route index element={<CatalogDetail />} />
      <Route path="edit" element={<CatalogUpdate />} />
      <Route path="delete" element={<CatalogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CatalogRoutes;
