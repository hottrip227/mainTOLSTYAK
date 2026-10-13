from PIL import Image,ImageDraw
from pathlib import Path
import json,random,math
root=Path(__file__).resolve().parents[1];assets=root/'src/main/resources/assets/ashes_of_archmage';rng=random.Random(227)
(assets/'textures/entity').mkdir(parents=True,exist_ok=True);(assets/'textures/item').mkdir(parents=True,exist_ok=True)
# Editable 64x64 Minecraft skin; no Valve artwork is embedded.
skin=Image.new('RGBA',(64,64),(0,0,0,0));d=ImageDraw.Draw(skin)
purple=(65,29,73,255);dark=(29,23,38,255);gold=(201,157,75,255);silver=(218,220,232,255);pale=(213,184,169,255)
def fill(x,y,w,h,c):
 for yy in range(y,y+h):
  for xx in range(x,x+w):
   delta=rng.choice([-4,-2,0,0,2,3]);skin.putpixel((xx,yy),tuple(max(0,min(255,v+delta)) for v in c[:3])+(255,))
def cube(u,v,w,h,z,c):
 for x,y,a,b in [(u+z,v,w,z),(u+z+w,v,w,z),(u,v+z,z,h),(u+z,v+z,w,h),(u+z+w,v+z,z,h),(u+2*z+w,v+z,w,h)]:fill(x,y,a,b,c)
cube(0,0,8,8,8,silver);fill(8,8,8,8,pale)
# Silver hairline, a stern pale face and violet eyes.
d.rectangle((8,8,15,9),fill=silver);d.line((8,10,8,13),fill=(192,196,210));d.line((15,10,15,13),fill=(229,229,237))
d.line((10,11,11,11),fill=(94,77,89));d.line((13,11,14,11),fill=(94,77,89));d.point((10,12),fill=(177,93,218));d.point((13,12),fill=(177,93,218));d.point((11,12),fill=(247,225,248));d.point((14,12),fill=(247,225,248));d.point((12,13),fill=(178,142,129));d.line((11,14,13,14),fill=(125,94,101))
for x in [0,2,5,16,18,21,24,27,30]:d.line((x,8,x,15),fill=(175,180,199))
cube(16,16,8,12,4,purple)
# Gold lapels and belt; dark chest inset with amethyst clasp.
d.polygon([(20,20),(22,20),(24,25),(26,20),(27,20),(24,28)],fill=gold);d.polygon([(22,21),(24,26),(26,21)],fill=dark)
d.rectangle((20,29,27,30),fill=gold);d.rectangle((23,28,24,30),fill=(30,21,39));d.point((23,29),fill=(180,79,197))
for u,v in [(40,16),(32,48)]:
 cube(u,v,4,12,4,dark)
 for xx in [u+4,u+5,u+6,u+7]:
  d.point((xx,v+5),fill=gold);d.point((xx,v+12),fill=gold);d.point((xx,v+13),fill=gold)
 fill(u+4,v+14,4,2,pale)
for u,v in [(0,16),(16,48)]:
 cube(u,v,4,12,4,purple);d.rectangle((u+4,v+12,u+7,v+15),fill=dark);d.line((u+4,v+12,u+7,v+12),fill=gold)
 d.line((u+4,v+4,u+4,v+11),fill=gold)
skin.save(assets/'textures/entity/archmage.png')
# Material atlas for genuine 3D shoulder pieces, collar, cape and hair.
palette={'onyx':(30,24,39),'plum':(66,27,74),'violet':(103,43,112),'gold':(203,158,73),'gold_light':(235,193,105),'silver':(214,218,232),'silver_light':(243,240,246),'silver_dark':(159,169,193)}
atlas=Image.new('RGBA',(256,256),(30,24,39,255));ad=ImageDraw.Draw(atlas);indices={}
for i,(name,c) in enumerate(palette.items()):
 indices[name]=i;x=(i%4)*64;y=(i//4)*64
 for yy in range(y,y+64):
  for xx in range(x,x+64):
   delta=rng.choice([-2,0,0,0,1,2]);atlas.putpixel((xx,yy),tuple(max(0,min(255,v+delta)) for v in c)+(255,))
atlas.save(assets/'textures/entity/archmage_outfit.png')
# One shared geometry description generates both the Java model and its preview.
boxes=[]
def box(part,name,x,y,z,w,h,dep,material,rotation=0):
 boxes.append(dict(part=part,name=name,from_=[x,y,z],size=[w,h,dep],material=material,uv=[(indices[material]%4)*64,(indices[material]//4)*64],rotation_z=rotation))
box('body','collar_back',-6.1,-5.2,2.6,12.2,6,2,'onyx');box('body','collar_back_inlay',-5.6,-4.7,2.45,11.2,5,0.25,'violet');box('body','collar_crown',-6.2,-5.5,2.4,12.4,0.5,2.4,'gold')
for side in [-1,1]:
 x=-6.2 if side<0 else 4.5
 box('body',f'collar_side_{side}',x,-4.3,-0.5,1.7,4.7,3.2,'onyx');box('body',f'collar_edge_{side}',x,-4.6,-0.6,1.7,0.5,3.4,'gold')
 part='right_arm' if side<0 else 'left_arm'
 x=-4.5 if side<0 else -1
 box(part,f'pauldron_{side}',x,-3.7,-3.3,5.5,2.2,6.6,'onyx');box(part,f'pauldron_gold_{side}',x,-4.0,-3.4,5.5,0.55,6.8,'gold')
 box(part,f'pauldron_inlay_{side}',x+0.5,-4.25,-2.8,4.5,0.45,5.6,'plum')
 # Long upward outer points, articulated with the arm.
 sx=-5.4 if side<0 else 3.9
 box(part,f'shoulder_spire_{side}',sx,-7.1,-1.5,1.3,5.2,3.0,'onyx',side*0.18)
 box(part,f'shoulder_spire_gold_{side}',sx,-7.4,-1.65,1.3,0.6,3.3,'gold_light',side*0.18)
 # Gauntlet cuff detail; palms remain visible on the skin.
 ax=-3.3 if side<0 else -1.3
 box(part,f'cuff_{side}',ax,7.1,-2.3,4.6,1.25,4.6,'gold');box(part,f'cuff_inlay_{side}',ax+0.2,7.35,-2.45,4.2,0.7,0.3,'plum')
# Silver swept-back silhouette and two unmistakable long front strands.
box('head','hair_crown',-4.25,-8.5,-3.9,8.5,1.15,8.4,'silver')
box('head','hair_back',-4.2,-7.7,3.35,8.4,13.5,1.25,'silver_dark')
for i,x in enumerate([-3.6,-1.8,0,1.8,3.6]):box('head',f'back_strand_{i}',x,-7.7,4.35,1.2,13.3-(i%2),0.35,'silver' if i%2 else 'silver_light')
for side in [-1,1]:
 x=-4.55 if side<0 else 3.6
 box('head',f'hair_side_{side}',x,-7.3,-2.0,0.95,9.5,5.6,'silver')
 x=-3.25 if side<0 else 2.05
 box('head',f'front_strand_{side}',x,-7.3,-4.55,1.2,12.1,0.9,'silver')
 box('head',f'front_strand_light_{side}',x+0.15,-7.3,-4.7,0.45,11.6,0.2,'silver_light')
# Cape attached to torso; a small moving angle opens space behind legs.
box('cape','cape_back',-6.1,1,3.15,12.2,21,0.65,'plum');box('cape','cape_lining',-5.5,1.5,3.0,11,19.8,0.2,'violet')
for x in [-6.15,5.6]:box('cape',f'cape_edge_{x}',x,1,2.95,0.55,21,1.0,'gold')
box('cape','cape_hem',-6.1,21.5,2.95,12.2,0.55,1.0,'gold')
for i,w in enumerate([1,2,3,2,1]):box('cape',f'memory_seal_{i}',-w/2,8+i,3.83,w,1,0.22,'gold')
box('cape','seal_gem',-0.55,9.8,4.02,1.1,1.4,0.18,'violet')
box('body','tabard',-1.9,9.7,-2.75,3.8,11.5,0.5,'plum');box('body','tabard_trim',-2.05,20.7,-2.85,4.1,0.55,0.7,'gold')
box('body','chest_jewel_frame',-1.2,1.5,-2.45,2.4,2.4,0.5,'gold');box('body','chest_jewel',-0.65,2.05,-2.75,1.3,1.3,0.5,'violet')
(root/'docs/visual-assets').mkdir(parents=True,exist_ok=True)
(root/'docs/visual-assets/outfit-geometry.json').write_text(json.dumps(boxes,indent=2)+'\n')
# Faceted round Quas mesh: 0.56-block diameter, not a particle cloud or a cube.
tex=Image.new('RGBA',(32,32));td=ImageDraw.Draw(tex)
for y in range(32):
 for x in range(32):
  n=rng.random();v=math.sin(x*.43+y*.29)+math.cos(y*.5-x*.31)
  tex.putpixel((x,y),(int(65+30*n+18*v),int(175+25*n+12*v),int(224+22*n+4*v),255))
for points in [[(0,8),(7,12),(10,18),(16,20),(20,30)],[(6,0),(9,5),(17,8),(21,14),(31,17)],[(13,32),(14,26),(22,23),(25,18)],[(0,25),(8,23),(12,17)]]:
 td.line(points,fill=(181,242,255),width=1)
 for x,y in points:td.point((x,y),fill=(240,255,255))
tex.save(assets/'textures/item/quas_orb.png')
elements=[];r=4.5;step=0.5
for yi in range(18):
 ya=-r+yi*step;yb=ya+step;yc=(ya+yb)/2
 for zi in range(18):
  za=-r+zi*step;zb=za+step;zc=(za+zb)/2
  if yc*yc+zc*zc>=r*r:continue
  xr=math.sqrt(r*r-yc*yc-zc*zc)
  frm=[8-xr,8+ya,8+za];to=[8+xr,8+yb,8+zb]
  def uv(a,b,c,e):return [round(a,4),round(b,4),round(c,4),round(e,4)]
  xx=lambda v:(v-(8-r))*16/(2*r)
  yy=lambda v:16-(v-(8-r))*16/(2*r)
  zz=lambda v:(v-(8-r))*16/(2*r)
  faces={
   'north':{'uv':uv(xx(frm[0]),yy(to[1]),xx(to[0]),yy(frm[1])),'texture':'#ice'},
   'south':{'uv':uv(xx(frm[0]),yy(to[1]),xx(to[0]),yy(frm[1])),'texture':'#ice'},
   'up':{'uv':uv(xx(frm[0]),zz(frm[2]),xx(to[0]),zz(to[2])),'texture':'#ice'},
   'down':{'uv':uv(xx(frm[0]),zz(frm[2]),xx(to[0]),zz(to[2])),'texture':'#ice'},
   'east':{'uv':uv(zz(frm[2]),yy(to[1]),zz(to[2]),yy(frm[1])),'texture':'#ice'},
   'west':{'uv':uv(zz(frm[2]),yy(to[1]),zz(to[2]),yy(frm[1])),'texture':'#ice'}}
  elements.append({'from':[round(v,5) for v in frm],'to':[round(v,5) for v in to],'shade':True,'light_emission':15,'faces':faces})
(assets/'models/item').mkdir(parents=True,exist_ok=True);(assets/'items').mkdir(exist_ok=True)
model={'textures':{'ice':'ashes_of_archmage:item/quas_orb','particle':'ashes_of_archmage:item/quas_orb'},'gui_light':'front','elements':elements}
(assets/'models/item/quas_orb.json').write_text(json.dumps(model,separators=(',',':'))+'\n')
(assets/'items/quas_orb.json').write_text(json.dumps({'model':{'type':'minecraft:model','model':'ashes_of_archmage:item/quas_orb'}},indent=2)+'\n')
# Generate maintainable Java geometry using the same cuboids as the preview.
lines=[]
for b in boxes:
 x,y,z=b['from_'];w,h,dep=b['size'];u,v=b['uv'];angle=b['rotation_z']
 fs=lambda n:format(n,'.5g')+'f'
 lines.append('        '+b['part']+'.addOrReplaceChild("'+b['name']+'", CubeListBuilder.create().texOffs('+str(u)+', '+str(v)+').addBox('+', '.join(map(fs,[x,y,z,w,h,dep]))+'), '+('PartPose.ZERO' if not angle else 'PartPose.rotation(0f, 0f, '+fs(angle)+')')+');')
java='''package dev.archmage.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

/** Original Minecraft-scale geometry; editable source: docs/visual-assets/outfit-geometry.json. */
public final class ArchmageOutfitGeometry {
    private ArchmageOutfitGeometry() {}
    public static ModelPart create() {
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();
        var head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        var body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        var left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        var right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        var cape = root.addOrReplaceChild("cape", CubeListBuilder.create(), PartPose.ZERO);
'''+ '\n'.join(lines)+'''
        return LayerDefinition.create(mesh, 256, 256).bakeRoot();
    }
}
'''
(root/'src/main/java/dev/archmage/client/ArchmageOutfitGeometry.java').write_text(java)
print('Generated skin, 3D outfit atlas,',len(boxes),'outfit parts and',len(elements),'Quas elements')
