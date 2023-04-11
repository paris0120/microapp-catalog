import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICatalog } from 'app/shared/model/catalog/catalog.model';
import { getEntity, updateEntity, createEntity, reset } from './catalog.reducer';

export const CatalogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const catalogEntity = useAppSelector(state => state.catalog.catalog.entity);
  const loading = useAppSelector(state => state.catalog.catalog.loading);
  const updating = useAppSelector(state => state.catalog.catalog.updating);
  const updateSuccess = useAppSelector(state => state.catalog.catalog.updateSuccess);

  const handleClose = () => {
    navigate('/catalog/catalog' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...catalogEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...catalogEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="catalogApp.catalogCatalog.home.createOrEditLabel" data-cy="CatalogCreateUpdateHeading">
            <Translate contentKey="catalogApp.catalogCatalog.home.createOrEditLabel">Create or edit a Catalog</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="catalog-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.name')}
                id="catalog-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.hover')}
                id="catalog-hover"
                name="hover"
                data-cy="hover"
                type="text"
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.description')}
                id="catalog-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.color')}
                id="catalog-color"
                name="color"
                data-cy="color"
                type="text"
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.link')}
                id="catalog-link"
                name="link"
                data-cy="link"
                type="text"
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.icon')}
                id="catalog-icon"
                name="icon"
                data-cy="icon"
                type="text"
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.weight')}
                id="catalog-weight"
                name="weight"
                data-cy="weight"
                type="text"
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.groupUuid')}
                id="catalog-groupUuid"
                name="groupUuid"
                data-cy="groupUuid"
                type="text"
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.isActive')}
                id="catalog-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('catalogApp.catalogCatalog.type')}
                id="catalog-type"
                name="type"
                data-cy="type"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/catalog/catalog" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CatalogUpdate;
