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
import {getEntities, getGroupList, getGroupTypeList} from './catalog.reducer';
import {Form, Select} from "antd";
import CatalogEdit from "app/entities/catalog/catalog/catalog-edit";

export const Catalog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const [groupUuid, setGroupUuid] = useState(null)
  const [type, setType] = useState(null)

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const catalogList = useAppSelector(state => state.catalog.catalog.entities);
  const loading = useAppSelector(state => state.catalog.catalog.loading);
  const totalItems = useAppSelector(state => state.catalog.catalog.totalItems);
  const groups = useAppSelector(state => state.catalog.catalog.catalog.groups);
  const types = useAppSelector(state => state.catalog.catalog.catalog.types);


  useEffect(()=>{
    dispatch(getGroupList())
  },[])

  useEffect(()=>{
    if(groupUuid!=null) {
      dispatch(getGroupTypeList(groupUuid))
    }
  }, [groupUuid])


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

      </h2>
        <div className="d-flex justify-content-between">
          <Form
            name="basic"
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
            style={{ maxWidth: 600 }}
            initialValues={{ remember: true }}
            autoComplete="off"
          >

          <Form.Item label='Group'>

          <Select title='group'
            defaultValue="Select a group"
            style={{ width: 500 }}
            onSelect={(value) => {setGroupUuid(value)}}
            options={groups}
          />
          </Form.Item>
          {groupUuid?<Form.Item label="Type">
            <Select title='type'
                defaultValue="Select a group"
                style={{ width: 500 }}
                onSelect={(value) => {setType(value)}}
                options={types}
            />
          </Form.Item>
            :<></>}
          </Form>
        </div>

      {type?<CatalogEdit type={type} groupUuid={groupUuid}/>:<></>}

    </div>
  );
};

export default Catalog;
