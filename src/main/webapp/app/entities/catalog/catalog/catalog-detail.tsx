import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './catalog.reducer';

export const CatalogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const catalogEntity = useAppSelector(state => state.catalog.catalog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="catalogDetailsHeading">
          <Translate contentKey="catalogApp.catalogCatalog.detail.title">Catalog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="catalogApp.catalogCatalog.name">Name</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.name}</dd>
          <dt>
            <span id="hover">
              <Translate contentKey="catalogApp.catalogCatalog.hover">Hover</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.hover}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="catalogApp.catalogCatalog.description">Description</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.description}</dd>
          <dt>
            <span id="color">
              <Translate contentKey="catalogApp.catalogCatalog.color">Color</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.color}</dd>
          <dt>
            <span id="link">
              <Translate contentKey="catalogApp.catalogCatalog.link">Link</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.link}</dd>
          <dt>
            <span id="icon">
              <Translate contentKey="catalogApp.catalogCatalog.icon">Icon</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.icon}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="catalogApp.catalogCatalog.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.weight}</dd>
          <dt>
            <span id="groupUuid">
              <Translate contentKey="catalogApp.catalogCatalog.groupUuid">Group Uuid</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.groupUuid}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="catalogApp.catalogCatalog.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="catalogApp.catalogCatalog.type">Type</Translate>
            </span>
          </dt>
          <dd>{catalogEntity.type}</dd>
        </dl>
        <Button tag={Link} to="/catalog/catalog" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/catalog/catalog/${catalogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CatalogDetail;
