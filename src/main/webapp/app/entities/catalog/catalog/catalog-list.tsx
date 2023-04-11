import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICatalog } from 'app/shared/model/catalog/catalog.model';
import {getEntities, getGroupList, getGroupTypeCatalog, getGroupTypeList} from './catalog.reducer';
import {Form, Select} from "antd";

export const CatalogList = (prop) => {
  console.log(prop)
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const groupUuid = prop.groupUuid
  const type = prop.type

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );
  const catalogList = useAppSelector(state => state.catalog.catalog.entities);
  const loading = useAppSelector(state => state.catalog.catalog.loading);
  const totalItems = useAppSelector(state => state.catalog.catalog.totalItems);


  useEffect(() => {
    dispatch(getGroupTypeCatalog({groupUuid, type}))
  }, [])
  const getAllEntities = () => {
    dispatch(
      getEntities({
        groupUuid,
        type
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);

    }
  };



  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
       <h2 id="catalog-heading" data-cy="CatalogHeading">
        <Translate contentKey="catalogApp.catalogCatalog.home.title">Catalogs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="catalogApp.catalogCatalog.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/catalog/catalog/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="catalogApp.catalogCatalog.home.createLabel">Create new Catalog</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {catalogList && catalogList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="catalogApp.catalogCatalog.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="catalogApp.catalogCatalog.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('hover')}>
                  <Translate contentKey="catalogApp.catalogCatalog.hover">Hover</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="catalogApp.catalogCatalog.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('color')}>
                  <Translate contentKey="catalogApp.catalogCatalog.color">Color</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('link')}>
                  <Translate contentKey="catalogApp.catalogCatalog.link">Link</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('icon')}>
                  <Translate contentKey="catalogApp.catalogCatalog.icon">Icon</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('weight')}>
                  <Translate contentKey="catalogApp.catalogCatalog.weight">Weight</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('groupUuid')}>
                  <Translate contentKey="catalogApp.catalogCatalog.groupUuid">Group Uuid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="catalogApp.catalogCatalog.isActive">Is Active</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="catalogApp.catalogCatalog.type">Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {catalogList.map((catalog, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/catalog/catalog/${catalog.id}`} color="link" size="sm">
                      {catalog.id}
                    </Button>
                  </td>
                  <td>{catalog.name}</td>
                  <td>{catalog.hover}</td>
                  <td>{catalog.description}</td>
                  <td>{catalog.color}</td>
                  <td>{catalog.link}</td>
                  <td>{catalog.icon}</td>
                  <td>{catalog.weight}</td>
                  <td>{catalog.groupUuid}</td>
                  <td>{catalog.isActive ? 'true' : 'false'}</td>
                  <td>{catalog.type}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/catalog/catalog/${catalog.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/catalog/catalog/${catalog.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/catalog/catalog/${catalog.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="catalogApp.catalogCatalog.home.notFound">No Catalogs found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={catalogList && catalogList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default CatalogList;
