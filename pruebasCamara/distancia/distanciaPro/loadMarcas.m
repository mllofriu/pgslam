function [depuradas,hmax,dist,tam] = loadMarcas(marcas,umbralConf)

depuradas = [];
for i = 1:length(marcas),
    if marcas(i,16) > umbralConf
        depuradas = [depuradas; marcas(i,:)];
    end
end

h = [depuradas(:,2)+depuradas(:,4), depuradas(:,7)+depuradas(:,9), depuradas(:,12)+depuradas(:,14)];
hmax = max(h, [], 2);
heigth = [depuradas(:,4),depuradas(:,9),depuradas(:,14)];
tam = median(heigth,2);
dist = depuradas(:,17);

depuradas2 = [];
for i = 1:length(depuradas),

    if depuradas(i,17) > 400 & tam(i) < 20 & tam(i) > 9,
        depuradas2 = [depuradas2; depuradas(i,:)];
    end
end
depuradas = depuradas2;

h = [depuradas(:,2)+depuradas(:,4), depuradas(:,7)+depuradas(:,9), depuradas(:,12)+depuradas(:,14)];
hmax = max(h, [], 2);
heigth = [depuradas(:,4),depuradas(:,9),depuradas(:,14)];
tam = median(heigth,2);
dist = depuradas(:,17);

