-- V2__add_full_text_search_indexing.sql
CREATE EXTENSION IF NOT EXISTS unaccent;

-- Créer une fonction de génération de vecteur de recherche
CREATE OR REPLACE FUNCTION note_search_vector_update() RETURNS trigger AS $$
BEGIN
    NEW.search_vector = to_tsvector('french', unaccent(NEW.title) || ' ' || unaccent(NEW.content));
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

-- Ajouter une colonne de vecteur de recherche à la table notes
ALTER TABLE notes ADD COLUMN search_vector tsvector;

-- Créer un index GIN sur la colonne de vecteur de recherche
CREATE INDEX idx_notes_search_vector ON notes USING GIN(search_vector);

-- Mettre à jour tous les enregistrements existants
UPDATE notes SET search_vector = to_tsvector('french', unaccent(title) || ' ' || unaccent(content));

-- Créer un déclencheur pour maintenir automatiquement le vecteur de recherche
CREATE TRIGGER tsvector_update_trigger
    BEFORE INSERT OR UPDATE ON notes
    FOR EACH ROW EXECUTE FUNCTION note_search_vector_update();