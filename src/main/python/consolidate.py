import argparse
import csv
import yaml

class Consolidator:
    def __init__(self):
        self.synonyms = dict()
        self.data = dict()

    def register_synonyms(self, real, synonyms):
        for synonym in synonyms:
            self.synonyms[synonym] = real

    def register(self, synonyms):
        for key in synonyms:
            for synonym in synonyms[key]:
                self.synonyms[synonym] = key

    def add(self, key, number, size):
        consolidated_key = self.synonyms.get(key, key)
        old_number, old_size = self.data.get(consolidated_key, (0, 0))
        self.data[consolidated_key] = (old_number + number, old_size + size)
        # if consolidated_key in self.data:
        #     old_number, old_size = self.data[consolidated_key]
        #     self.data[consolidated_key] = (old_number + number, old_size + size)
        # else:
        #     self.data[consolidated_key] = (number, size)


def main(source, target):

    consolidator = Consolidator()
    with open('mapping.yml', 'r') as config_file:
        config = yaml.load(config_file)
        consolidator.register(config['mappings'])

    with open(source, 'r', newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=';')
        first_line = True
        for line in reader:
            if first_line:
                first_line = False
                continue
            consolidator.add(line[0], int(line[1]), int(line[2]))

    with open(target, 'w') as out:
        for mime in sorted(consolidator.data):
            print(mime, consolidator.data[mime][0], consolidator.data[mime][1], sep=";", file=out)



if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Consolidate media type reports.')
    parser.add_argument('source', help='The report to consolidate.')
    parser.add_argument('description', help='Filename of the consolidated report.')
    args = parser.parse_args()
    main(args.source, args.description)
