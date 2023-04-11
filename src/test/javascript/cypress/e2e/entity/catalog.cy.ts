import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Catalog e2e test', () => {
  const catalogPageUrl = '/catalog/catalog';
  const catalogPageUrlPattern = new RegExp('/catalog/catalog(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const catalogSample = { name: 'Concrete generating Wyoming' };

  let catalog;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/catalog/api/catalogs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/catalog/api/catalogs').as('postEntityRequest');
    cy.intercept('DELETE', '/services/catalog/api/catalogs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (catalog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/catalog/api/catalogs/${catalog.id}`,
      }).then(() => {
        catalog = undefined;
      });
    }
  });

  it('Catalogs menu should load Catalogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalog/catalog');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Catalog').should('exist');
    cy.url().should('match', catalogPageUrlPattern);
  });

  describe('Catalog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(catalogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Catalog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/catalog/catalog/new$'));
        cy.getEntityCreateUpdateHeading('Catalog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/catalog/api/catalogs',
          body: catalogSample,
        }).then(({ body }) => {
          catalog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/catalog/api/catalogs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/catalog/api/catalogs?page=0&size=20>; rel="last",<http://localhost/services/catalog/api/catalogs?page=0&size=20>; rel="first"',
              },
              body: [catalog],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(catalogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Catalog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('catalog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogPageUrlPattern);
      });

      it('edit button click should load edit Catalog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Catalog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogPageUrlPattern);
      });

      it('edit button click should load edit Catalog page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Catalog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogPageUrlPattern);
      });

      it('last delete button click should delete instance of Catalog', () => {
        cy.intercept('GET', '/services/catalog/api/catalogs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('catalog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogPageUrlPattern);

        catalog = undefined;
      });
    });
  });

  describe('new Catalog page', () => {
    beforeEach(() => {
      cy.visit(`${catalogPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Catalog');
    });

    it('should create an instance of Catalog', () => {
      cy.get(`[data-cy="name"]`).type('Loan metrics').should('have.value', 'Loan metrics');

      cy.get(`[data-cy="hover"]`).type('Small').should('have.value', 'Small');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="color"]`).type('lime').should('have.value', 'lime');

      cy.get(`[data-cy="link"]`).type('Glen Corner Down-sized').should('have.value', 'Glen Corner Down-sized');

      cy.get(`[data-cy="icon"]`).type('frame tan bus').should('have.value', 'frame tan bus');

      cy.get(`[data-cy="weight"]`).type('59792').should('have.value', '59792');

      cy.get(`[data-cy="groupUuid"]`)
        .type('bc5088a0-c67b-45fb-8a62-9e8c1f633b98')
        .invoke('val')
        .should('match', new RegExp('bc5088a0-c67b-45fb-8a62-9e8c1f633b98'));

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click().should('be.checked');

      cy.get(`[data-cy="type"]`).type('Plastic Chicken').should('have.value', 'Plastic Chicken');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        catalog = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', catalogPageUrlPattern);
    });
  });
});
